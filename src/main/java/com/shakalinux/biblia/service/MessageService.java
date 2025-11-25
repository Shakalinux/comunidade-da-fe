package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.Message;
import com.shakalinux.biblia.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getPrivateMessages(String user1, String user2) {
        return messageRepository
                .findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(
                        user1, user2,
                        user2, user1
                );
    }

    public List<Message> getPublicMessages() {
        return messageRepository.findByReceiverIsNullOrderByTimestamp();
    }

    public void savePrivateMessage(String sender, String receiver, String texto) {
        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(texto);
        messageRepository.save(msg);
    }

    public List<Message> findChatMessagesBetween(String user1, String user2) {
        return messageRepository.findChatMessagesBetween(user1, user2);
    }

    public long countUnreadMessages(String receiverUsername) {
        return messageRepository.countByReceiverAndReadflagFalse(receiverUsername);
    }
    @Transactional
    public void markMessagesAsRead(String receiver, String sender) {
        messageRepository.markAsRead(receiver, sender);
    }

    public long countUnreadMessagesFromSender(String receiverUsername, String senderUsername) {
        return messageRepository.countByReceiverAndSenderAndReadflagFalse(receiverUsername, senderUsername);
    }

    public List<Integer> findSentMessageIds(String reader, String sender) {
        return messageRepository.findMessageIdsByReceiverAndSender(reader, sender);
    }


}
