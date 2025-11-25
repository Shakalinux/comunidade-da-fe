package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.*;
import com.shakalinux.biblia.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class BibliaService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private VersiculoRepository versiculoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcessRepository acessoRepository;

    @Autowired
    private VersaoRepository versaoRepository;

    @Autowired
    private CapituloRepository capituloRepository;


    private static Versiculo versiculoDoDiaCache = null;
    private static LocalDate dataGeracao = null;
    @Transactional(readOnly = true)
    public Versiculo getVersiculoDoDia() {
        LocalDate hoje = LocalDate.now();

        if (versiculoDoDiaCache == null || !hoje.isEqual(dataGeracao)) {
            Optional<Versiculo> novoVersiculo = versiculoRepository.findRandomVersiculo();

            if (novoVersiculo.isPresent()) {
                versiculoDoDiaCache = novoVersiculo.get();
                dataGeracao = hoje;
            } else {
                throw new RuntimeException("Não foi possível encontrar um versículo para o dia.");
            }
        }
        return versiculoDoDiaCache;
    }

    public List<Livro> getLivrosPorTestamento(String nomeTestamento) {
        return livroRepository.findByTestamentoNome(nomeTestamento);
    }

    public Optional<Livro> getLivroPorNome(String nome) {
        return livroRepository.findByNome(nome);
    }

    public List<Versiculo> getVersiculosPorCapitulo(String livroNome, Integer capitulo, Integer versaoId) {
        return versiculoRepository.findByLivroNomeAndCapituloAndVersaoId(livroNome, capitulo, versaoId);
    }

    public List<Versiculo> getVersiculosPorRange(String livroNome, Integer capitulo, Integer inicio, Integer fim) {
        return versiculoRepository.findVersiculosPorRange(livroNome, capitulo, inicio, fim);
    }

    public List<Livro> getTodosLivros() {
        return livroRepository.findAll();
    }

    @Transactional
    public void registrarAcessoDiario(User user) {
        if (user == null) return;

        LocalDate hoje = LocalDate.now();

        Optional<Acess> acessoExistente = acessoRepository.findTopByUserAndDataAcesso(user, hoje);

        if (acessoExistente.isEmpty()) {

            Acess acesso = new Acess();
            acesso.setUser(user);
            acesso.setDataAcesso(hoje);

            acessoRepository.save(acesso);
        }
    }

    @Transactional
    public void registrarAcesso(String username, Livro livro) {
        User user = userRepository.findByUsername(username);
        if (user != null && livro != null) {
            LocalDate hoje = LocalDate.now();


            Optional<Acess> acessoExistente = acessoRepository.findByUserAndDataAcessoAndLivro(user, hoje, livro);

            if (acessoExistente.isEmpty()) {
                Acess acesso = new Acess();
                acesso.setUser(user);
                acesso.setLivro(livro);
                acesso.setDataAcesso(hoje);
                acessoRepository.save(acesso);
            }
        }
    }




    public List<Livro> getUltimasLeituras(String username) {
        return acessoRepository.findUltimosLivrosLidosPorUsuario(username);
    }

    public List<Versao> getTodasVersoes() {
        return versaoRepository.findAll();
    }

    public boolean temProximoCapitulo(String livroNome, int capituloAtual) {
        Optional<Livro> livroOpt = livroRepository.findByNome(livroNome);
        if (livroOpt.isEmpty()) return false;

        List<Versiculo> versiculosProx =
            versiculoRepository.findByLivroNomeAndCapituloAndVersaoId(livroNome, capituloAtual + 1, 1);

        return versiculosProx != null && !versiculosProx.isEmpty();
    }

    public Optional<Livro> getLivroPorId(Long id) {
        return livroRepository.findById(id);
    }

    public List<Livro> buscarLivrosPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return getTodosLivros();
        }
        return livroRepository.findByNomeContainingIgnoreCase(nome);
    }

    public long countLivros() {
        return livroRepository.count();
    }

    public Long getTotalCapitulos(Livro livro) {
        if (livro == null) {
            return 0L;
        }

        Optional<Integer> maxCapituloOpt = versiculoRepository.findMaxCapituloByLivro(livro);

        return maxCapituloOpt.map(Long::valueOf).orElse(0L);
    }
}
