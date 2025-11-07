package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Capitulo;
import com.shakalinux.biblia.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapituloRepository extends JpaRepository<Capitulo, Long> {

    long countByLivro(Livro livro);
}
