package com.shakalinux.biblia.controller;

import com.shakalinux.biblia.model.*;
import com.shakalinux.biblia.service.BibliaService;
import com.shakalinux.biblia.service.LeituraUsuarioService;
import com.shakalinux.biblia.service.UserService;
import com.shakalinux.biblia.service.VersiculoSalvoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/biblia")
public class BibliaController {

    @Autowired
    private BibliaService bibliaService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeituraUsuarioService leituraUsuarioService;

    @Autowired
    private VersiculoSalvoService versiculoSalvoService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/biblioteca")
    public String biblioteca(Model model) {
        User usuario = userService.getLoggedInUser();
        if (usuario == null) return "redirect:/user/login";

        Profile profile = usuario.getProfile();
        model.addAttribute("profile", profile);

        model.addAttribute("ultimasLeituras", bibliaService.getUltimasLeituras(usuario.getUsername()));
        model.addAttribute("livros", bibliaService.getTodosLivros());
        model.addAttribute("versoes", bibliaService.getTodasVersoes());

        return "pageBiblia/biblioteca";
    }

    @GetMapping("/leitura/{livroId}/{capitulo}")
    public String leitura(
        @PathVariable Long livroId,
        @PathVariable Integer capitulo,
        @RequestParam(defaultValue = "1") Integer versaoId,
        @RequestParam(defaultValue = "1") int pagina,
        Model model) {

        Optional<Livro> livroOpt = bibliaService.getLivroPorId(livroId);
        if (livroOpt.isEmpty()) {
            model.addAttribute("erro", "Livro não encontrado.");
            return "pageBiblia/error";
        }

        Livro livro = livroOpt.get();
        List<Versiculo> todosVersiculos = bibliaService.getVersiculosPorCapitulo(livro.getNome(), capitulo, versaoId);
        if (todosVersiculos == null || todosVersiculos.isEmpty()) {
            model.addAttribute("erro", "Capítulo não encontrado nesta versão.");
            model.addAttribute("livro", livro);
            model.addAttribute("versaoId", versaoId);
            return "pageBiblia/leitura";
        }

        int versiculosPorPagina = 20;
        int totalPaginas = (int) Math.ceil((double) todosVersiculos.size() / versiculosPorPagina);
        pagina = Math.max(1, Math.min(pagina, totalPaginas));

        int inicio = (pagina - 1) * versiculosPorPagina;
        int fim = Math.min(inicio + versiculosPorPagina, todosVersiculos.size());
        List<Versiculo> versiculos = todosVersiculos.subList(inicio, fim);

        User usuario = userService.getLoggedInUser();
        if (usuario != null && usuario.getProfile() != null) {
            model.addAttribute("profile", usuario.getProfile());

            List<Long> favoritosIds = versiculoSalvoService.getFavoritedVersiculoIds(usuario, versiculos);
            model.addAttribute("favoritosIds", favoritosIds);

            bibliaService.registrarAcesso(usuario.getUsername(), livro);

            int totalCapitulos = Math.toIntExact(bibliaService.getTotalCapitulos(livro));
            model.addAttribute("totalCapitulos", totalCapitulos);
            if (totalCapitulos > 0) {
                leituraUsuarioService.atualizarProgresso(usuario, livro, capitulo, totalCapitulos);
            }
        } else {
            model.addAttribute("favoritosIds", Collections.emptyList());
        }

        Integer capituloAnterior = (capitulo > 1) ? capitulo - 1 : null;
        Integer capituloProximo = bibliaService.temProximoCapitulo(livro.getNome(), capitulo) ? capitulo + 1 : null;

        model.addAttribute("livro", livro);
        model.addAttribute("versiculos", versiculos);
        model.addAttribute("capitulo", capitulo);
        model.addAttribute("versaoId", versaoId);
        model.addAttribute("pagina", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("versoes", bibliaService.getTodasVersoes());
        model.addAttribute("capituloAnterior", capituloAnterior);
        model.addAttribute("capituloProximo", capituloProximo);

        return "pageBiblia/leitura";
    }


    @GetMapping("/versiculo/dia")
    @ResponseBody
    public Versiculo getVersiculoDoDia() {
        Versiculo versiculo = bibliaService.getVersiculoDoDia();
        if (versiculo == null) {
            throw new RuntimeException("Nenhum versículo encontrado");
        }

        return versiculo;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inicio")
    public String home(Model model) {
        User usuario = userService.getLoggedInUser();
        if (usuario == null) {
            return "redirect:/user/login";
        }

        Profile profile = usuario.getProfile() != null ? usuario.getProfile() : new Profile();
        List<Livro> ultimasLeituras = bibliaService.getUltimasLeituras(usuario.getUsername());
        if (ultimasLeituras == null) ultimasLeituras = List.of();

        Versiculo versiculoDoDia = bibliaService.getVersiculoDoDia();


        model.addAttribute("profile", profile);
        model.addAttribute("ultimasLeituras", ultimasLeituras);
        model.addAttribute("versiculoDoDia", versiculoDoDia);


        return "home";
    }

}


