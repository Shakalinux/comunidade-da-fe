package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Versao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersaoRepository extends JpaRepository<Versao, Long> {
}
