package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.LeituraUsuario;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.model.Livro;
import com.shakalinux.biblia.repository.LeituraUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeituraUsuarioService {

    @Autowired
    private LeituraUsuarioRepository leituraUsuarioRepository;

    @Transactional
    public void atualizarProgresso(User user, Livro livro, Integer capituloAtual, Integer totalCapitulos) {


        Optional<LeituraUsuario> leituraOpt = leituraUsuarioRepository.findByUserAndLivro(user, livro);

        LeituraUsuario leitura;

        if (leituraOpt.isPresent()) {

            leitura = leituraOpt.get();
        } else {

            leitura = new LeituraUsuario();
            leitura.setUser(user);
            leitura.setLivro(livro);
            leitura.setTotalCapitulos(totalCapitulos);
        }


        leitura.setCapituloAtual(capituloAtual);
        leitura.setUltimaAtualizacao(LocalDateTime.now());


        double progresso = ((double) capituloAtual / totalCapitulos) * 100;
        leitura.setProgressoPercentual((int) Math.min(progresso, 100));


        leituraUsuarioRepository.save(leitura);
    }


    @Transactional(readOnly = true)
    public List<LeituraUsuario> getTop3UltimasLeituras(User user) {

        return leituraUsuarioRepository.findTop3ByUserOrderByUltimaAtualizacaoDesc(user);
    }

}
