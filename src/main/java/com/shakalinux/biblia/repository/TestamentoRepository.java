package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Testamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestamentoRepository extends JpaRepository<Testamento, Integer> {
    Testamento findByNomeIgnoreCase(String nome);
}
