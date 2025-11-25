package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile WHERE u.username = :username")
    User findByUsername(@Param("username") String username);
    User findByEmail(String email);

}
