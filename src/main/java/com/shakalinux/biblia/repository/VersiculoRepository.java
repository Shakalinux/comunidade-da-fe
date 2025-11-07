package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Livro;
import com.shakalinux.biblia.model.Versiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersiculoRepository extends JpaRepository<Versiculo, Long> {
    List<Versiculo> findByLivroNomeAndCapitulo(String livroNome, Integer capitulo);  // Versículos de um capítulo
    @Query("SELECT v FROM Versiculo v WHERE v.livro.nome = :livroNome AND v.capitulo = :capitulo AND v.numero BETWEEN :inicio AND :fim")
    List<Versiculo> findVersiculosPorRange(@Param("livroNome") String livroNome,
                                           @Param("capitulo") Integer capitulo,
                                           @Param("inicio") Integer inicio,
                                           @Param("fim") Integer fim);

    List<Versiculo> findByLivroNomeAndCapituloAndVersaoId(String livroNome, Integer capitulo, Integer versaoId);


    @Query(value =
        "SELECT * FROM versiculos " +
            "WHERE ver_vrs_id NOT IN (11, 12, 13) " +
            "ORDER BY RAND() LIMIT 1",
        nativeQuery = true)
    Optional<Versiculo> findRandomVersiculo();


    @Query("SELECT MAX(v.capitulo) FROM Versiculo v WHERE v.livro = :livro")
    Optional<Integer> findMaxCapituloByLivro(@Param("livro") Livro livro);


}
