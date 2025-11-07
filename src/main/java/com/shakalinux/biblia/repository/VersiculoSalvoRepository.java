package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.model.Versiculo;
import com.shakalinux.biblia.model.VersiculoSalvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VersiculoSalvoRepository extends JpaRepository<VersiculoSalvo, Long> {


    List<VersiculoSalvo> findByUser(User user);

    Optional<VersiculoSalvo> findByUserAndVersiculo(User user, Versiculo versiculo);

    long countByUser(User user);

    List<VersiculoSalvo> findByUserAndVersiculoIn(User user, List<Versiculo> versiculos);

    @Query("""
SELECT vs FROM VersiculoSalvo vs
JOIN FETCH vs.versiculo v
JOIN FETCH v.livro
WHERE vs.user = :user
""")
    List<VersiculoSalvo> findByUserWithVersiculoAndLivro(@Param("user") User user);

    Optional<VersiculoSalvo> findByIdAndUser(Long id, User user);


}
