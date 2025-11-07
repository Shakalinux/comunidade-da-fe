package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.model.Versiculo;
import com.shakalinux.biblia.model.VersiculoSalvo;
import com.shakalinux.biblia.repository.VersiculoRepository;
import com.shakalinux.biblia.repository.VersiculoSalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VersiculoSalvoService {

    @Autowired
    private VersiculoSalvoRepository favoritoRepository;

    @Autowired
    private VersiculoRepository versiculoRepository;


    @Transactional
    public boolean toggleFavorito(User user, Long versiculoId) {
        if (user == null || versiculoId == null) {
            throw new IllegalArgumentException("Usuário ou ID do Versículo não podem ser nulos.");
        }

        Optional<Versiculo> versiculoOpt = versiculoRepository.findById(versiculoId);
        if (versiculoOpt.isEmpty()) {
            throw new RuntimeException("Versículo não encontrado com ID: " + versiculoId);
        }
        Versiculo versiculo = versiculoOpt.get();

        Optional<VersiculoSalvo> favoritoOpt = favoritoRepository.findByUserAndVersiculo(user, versiculo);

        if (favoritoOpt.isPresent()) {

            favoritoRepository.delete(favoritoOpt.get());
            return false;
        } else {

            VersiculoSalvo novoFavorito = new VersiculoSalvo();
            novoFavorito.setUser(user);
            novoFavorito.setVersiculo(versiculo);
            favoritoRepository.save(novoFavorito);
            return true;
        }
    }


    @Transactional(readOnly = true)
    public List<VersiculoSalvo> findByUser(User user) {
        return favoritoRepository.findByUserWithVersiculoAndLivro(user);
    }


    @Transactional(readOnly = true)
    public long countFavoritosByUser(User user) {
        return favoritoRepository.countByUser(user);
    }

    @Transactional
    public void removerFavorito(User user, Long idVersiculoSalvo) {
        Optional<VersiculoSalvo> favoritoOpt = favoritoRepository.findById(idVersiculoSalvo);
        if (favoritoOpt.isPresent()) {
            VersiculoSalvo favorito = favoritoOpt.get();

            if (favorito.getUser().getId().equals(user.getId())) {
                favoritoRepository.delete(favorito);
            } else {
                throw new SecurityException("Acesso negado: Tentativa de remover favorito de outro usuário.");
            }
        } else {
            throw new RuntimeException("Favorito não encontrado.");
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getFavoritedVersiculoIds(User user, List<Versiculo> versiculosDoCapitulo) {
        if (user == null || versiculosDoCapitulo.isEmpty()) {
            return Collections.emptyList();
        }


        List<VersiculoSalvo> favoritos = favoritoRepository.findByUserAndVersiculoIn(user, versiculosDoCapitulo);

        return favoritos.stream()
            .map(vs -> vs.getVersiculo().getId())
            .collect(Collectors.toList());
    }

    @Transactional
    public void salvarReflexao(User user, Long idVersiculoSalvo, String reflexao) {
        if (user == null) {
            throw new SecurityException("Usuário não autenticado.");
        }
        VersiculoSalvo favorito = favoritoRepository.findByIdAndUser(idVersiculoSalvo, user)
            .orElseThrow(() -> new SecurityException("Acesso negado ou Versículo salvo não encontrado."));


        favorito.setReflexao(reflexao);
        favoritoRepository.save(favorito);
    }



}
