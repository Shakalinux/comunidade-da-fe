package com.shakalinux.biblia.controller;

import com.shakalinux.biblia.model.Estudo;
import com.shakalinux.biblia.model.Profile;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.service.EstudoService;
import com.shakalinux.biblia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/estudos")
@RequiredArgsConstructor
public class EstudoController {
    @Autowired
    private  EstudoService estudoService;
    @Autowired
    private  UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/biblicos")
    public String estudosBiblicos(Model model) {
        User user = userService.getLoggedInUser();
        if (user == null) return "redirect:/user/login";

        model.addAttribute("profile", user.getProfile());

        String temaAleatorio = estudoService.gerarEstudos();
        String devocionalMensagem = estudoService.gerarEstudo(temaAleatorio);

        model.addAttribute("temaDevocional", temaAleatorio);
        model.addAttribute("devocionalDia", devocionalMensagem);

        model.addAttribute("temas", estudoService.getTemas());
        model.addAttribute("meusEstudos", estudoService.findEstudos(user));

        return "pageBiblia/estudosBiblicos";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/gerar")
    @ResponseBody
    public Map<String, String> gerarEstudo(@RequestParam String tema) {
        try {
            String mensagem = estudoService.gerarEstudo(tema);
            if (mensagem == null || mensagem.isEmpty()) {
                mensagem = "Não foi possível gerar o estudo no momento. Tente novamente.";
            }
            return Map.of("tema", tema, "mensagem", mensagem);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("tema", tema, "mensagem", "Erro interno ao gerar estudo: " + e.getMessage());
        }
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/salvar")
    @ResponseBody
    public String salvarEstudo(@RequestParam String tema, @RequestParam String mensagem) {
        User user = userService.getLoggedInUser();
        if (user == null) return "Usuário não autenticado.";

        Estudo estudo = new Estudo();
        estudo.setTema(tema);
        estudo.setMensagem(mensagem);
        estudo.setUser(user);
        estudoService.save(estudo);
        return "Estudo salvo com sucesso!";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/salvos")
    public String verEstudosSalvos(Model model) {
        User user = userService.getLoggedInUser();
        if (user == null) return "redirect:/user/login";

        model.addAttribute("profile", user.getProfile());
        model.addAttribute("meusEstudos", estudoService.findEstudos(user));
        return "pageBiblia/estudosSalvos";
    }







}
