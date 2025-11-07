package com.shakalinux.biblia.controller;

import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.model.VersiculoSalvo;
import com.shakalinux.biblia.service.UserService;
import com.shakalinux.biblia.service.VersiculoSalvoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/favorites")
@PreAuthorize("isAuthenticated()")
public class FavoritoController {


    private static final Logger log = LoggerFactory.getLogger(FavoritoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private VersiculoSalvoService favoritoService;



    @GetMapping
    @Transactional
    public String listarFavoritos(Model model) {
        User user = userService.getLoggedInUser();
        if (user == null) return "redirect:/user/login";

        List<VersiculoSalvo> favoritos = favoritoService.findByUser(user);

        model.addAttribute("versiculosSalvos", favoritos);
        model.addAttribute("profile", user.getProfile());

        return "pageBiblia/versiculoSalvo";
    }


    @PostMapping("/salvar-reflexao/{id}")
    @ResponseBody
    @Transactional
    public Map<String, Object> salvarReflexao(@PathVariable Long id,
                                              @RequestParam("reflexao") String reflexao) {

        Map<String, Object> response = new HashMap<>();
        User user = userService.getLoggedInUser();

        if (user == null) {
            response.put("success", false);
            response.put("error", "Usuário não autenticado. Por favor, faça login.");
            return response;
        }

        try {
            favoritoService.salvarReflexao(user, id, reflexao);
            response.put("success", true);
            response.put("message", "Reflexão salva com sucesso!");
            return response;
        } catch (SecurityException e) {

            response.put("success", false);
            response.put("error", "Acesso negado: " + e.getMessage());
            return response;
        } catch (Exception e) {

            log.error("Erro ao salvar reflexão (ID: {}): {}", id, e.getMessage(), e);

            response.put("success", false);
            response.put("error", "Erro interno do servidor. Detalhes: " + e.getMessage());

            return response;
        }
    }

    @PostMapping("/remover/{id}")
    public String removerFavorito(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getLoggedInUser();
        if (user == null) return "redirect:/user/login";

        try {
            favoritoService.removerFavorito(user, id);
            redirectAttributes.addFlashAttribute("sucesso", "Versículo removido com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao remover favorito (ID: {}): {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover favorito.");
        }

        return "redirect:/favorites";
    }

    @PostMapping("/toggle-form")
    @ResponseBody
    @Transactional
    public Map<String, Object> toggleFavorito(@RequestParam Long versiculoId) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.getLoggedInUser();

        if (user == null) {
            response.put("success", false);
            response.put("error", "Usuário não autenticado.");
            return response;
        }

        try {
            boolean isFavorited = favoritoService.toggleFavorito(user, versiculoId);
            response.put("success", true);
            response.put("favorited", isFavorited);
            response.put("message", isFavorited ?
                "Versículo adicionado aos favoritos!" :
                "Versículo removido dos favoritos!");
        } catch (Exception e) {
            log.error("Erro ao alternar favorito (ID: {}): {}", versiculoId, e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Erro ao alternar favorito.");
        }

        return response;
    }


}
