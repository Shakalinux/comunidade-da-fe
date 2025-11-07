package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.Livro;
import com.shakalinux.biblia.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;


    public List<Livro> getTodosLivros() {
        return livroRepository.findAllByOrderByNomeAsc();
    }


    public Optional<Livro> getLivroPorNome(String nome) {
        return livroRepository.findByNome(nome);
    }

    public List<Livro> getUltimasLeituras() {
        List<Livro> todos = livroRepository.findAllByOrderByNomeAsc();
        return todos.size() > 10 ? todos.subList(0, 10) : todos;
    }
}
