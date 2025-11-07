package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Acess;
import com.shakalinux.biblia.model.Livro;
import com.shakalinux.biblia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AcessRepository extends JpaRepository<Acess, Long> {

    Optional<Acess> findTopByUserAndDataAcesso(User user, LocalDate dataAcesso);

    Optional<Acess> findByUserAndDataAcessoAndLivro(User user, LocalDate dataAcesso, Livro livro);

    List<Acess> findByUserAndDataAcessoBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Acess> findByUser(User user);

    @Query("""
    SELECT a.livro
    FROM Acess a
    WHERE a.user.username = :username
    AND a.dataAcesso = (
        SELECT MAX(a2.dataAcesso)
        FROM Acess a2
        WHERE a2.user = a.user AND a2.livro = a.livro
    )
    ORDER BY a.dataAcesso DESC
    """)
    List<Livro> findUltimosLivrosLidosPorUsuario(@Param("username") String username);
}
