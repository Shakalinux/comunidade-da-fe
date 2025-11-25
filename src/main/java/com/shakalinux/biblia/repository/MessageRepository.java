package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(String sender1, String receiver1, String sender2, String receiver2);

    List<Message> findByReceiverIsNullOrderByTimestamp();

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.receiver = :user2) OR " +
            "(m.sender = :user2 AND m.receiver = :user1) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findChatMessagesBetween(@Param("user1") String user1, @Param("user2") String user2);

    long countByReceiverAndReadflagFalse(String receiver);

    @Modifying
    @Query("UPDATE Message m SET m.readflag = true WHERE m.receiver = :receiver AND m.sender = :sender AND m.readflag = false")
    void markAsRead(@Param("receiver") String receiver, @Param("sender") String sender);


    @Query("SELECT m.id FROM Message m WHERE m.receiver = :reader AND m.sender = :sender AND m.readflag = true")
    List<Integer> findMessageIdsByReceiverAndSender(@Param("reader") String reader, @Param("sender") String sender);


    long countByReceiverAndSenderAndReadflagFalse(String receiver, String sender);
}
