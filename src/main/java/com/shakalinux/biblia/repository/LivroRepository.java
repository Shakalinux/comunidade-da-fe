package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l WHERE l.testamento.nome = :nome")
    List<Livro> findByTestamentoNome(@Param("nome") String nome);


    Optional<Livro> findByNome(String nome);
    List<Livro> findAllByOrderByNomeAsc();
    List<Livro> findByNomeContainingIgnoreCase(String nome);
}
