package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.LeituraUsuario;
import com.shakalinux.biblia.model.Livro;
import com.shakalinux.biblia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LeituraUsuarioRepository extends JpaRepository<LeituraUsuario, Long> {
    Optional<LeituraUsuario> findByUserAndLivro(User user, Livro livro);

    List<LeituraUsuario> findTop3ByUserOrderByUltimaAtualizacaoDesc(User user);
}
