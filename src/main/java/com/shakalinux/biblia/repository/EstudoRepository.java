package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Estudo;
import com.shakalinux.biblia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudoRepository extends JpaRepository<Estudo, Long> {
    List<Estudo> findByUser(User user);
}
