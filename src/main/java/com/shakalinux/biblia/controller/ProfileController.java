package com.shakalinux.biblia.controller;

import com.shakalinux.biblia.model.*;
import com.shakalinux.biblia.repository.EstudoRepository;
import com.shakalinux.biblia.repository.UserRepository;
import com.shakalinux.biblia.service.AcessService;
import com.shakalinux.biblia.service.ProfileService;
import com.shakalinux.biblia.service.UserService;
import com.shakalinux.biblia.service.LeituraUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.web.csrf.CsrfToken;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/profile")
public class ProfileController {

  @Autowired
  private ProfileService profileService;

  @Autowired
  private UserService userService;

  @Autowired
  private AcessService acessService;

  @Autowired
  private LeituraUsuarioService leituraUsuarioService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EstudoRepository estudoRepository;



  @GetMapping
  public String getProfile(Model model, HttpServletRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userRepository.findByUsername(username);

    if (user == null) {
      model.addAttribute("error", "Usuário não encontrado.");
      return "error";
    }
        acessService.registrarAcessoDiario(user);

    Profile profile = profileService.findByUser(user);
    if (profile == null) {
      profile = new Profile();
      profile.setUser(user);
      profileService.saveProfile(profile);
    }
    profileService.encodeImages(profile);
    model.addAttribute("profile", profile);
    model.addAttribute("user", user);
    model.addAttribute("estudos", estudoRepository.findByUser(user));


    List<Acess> acess = acessService.findByUser(user);
    Set<LocalDate> diasAcessados = new HashSet<>();
    for (Acess acesso : acess) {
      diasAcessados.add(acesso.getDataAcesso());
    }
    model.addAttribute("diasAcessados", diasAcessados.size());

    List<LeituraUsuario> ultimasLeituras = leituraUsuarioService.getTop3UltimasLeituras(user);
    model.addAttribute("ultimasLeituras", ultimasLeituras);


    long completedTasks = 0L;


    long inProgressTasks = ultimasLeituras.size();


    List<VersiculoSalvo> versiculosSalvos = profileService.getVersiculosUser(user);

    model.addAttribute("versiculosSalvos", versiculosSalvos);

      long totalVersiculosSalvos = versiculosSalvos.size();
      model.addAttribute("totalVersiculosSalvos", totalVersiculosSalvos);


    long totalTasks = completedTasks + inProgressTasks;

    model.addAttribute("completedTasks", completedTasks);
    model.addAttribute("inProgressTasks", inProgressTasks);
    model.addAttribute("totalTasks", totalTasks);


    CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
    model.addAttribute("_csrf", csrfToken);

    return "profile/profile";
  }


  @PostMapping("/updateProfile")
  public String updateProfile(@RequestParam(required = false) String nickname,
                @RequestParam(required = false) String versiculo,
                @RequestParam(required = false) String igreja,
                @RequestParam(required = false) String ministerio,
                @RequestParam(required = false) String interesses,
                @RequestParam(required = false) String oracao,
                @RequestParam(required = false) MultipartFile avatarFile,
                @RequestParam(required = false) MultipartFile imagePrincipalFile) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userRepository.findByUsername(username);
    if (user == null) return "redirect:/profile?error=userNotFound";

    Profile existingProfile = profileService.findByUser(user);
    if (existingProfile == null) {
      existingProfile = new Profile();
      existingProfile.setUser(user);
    }

    try {
      if (nickname != null && !nickname.isBlank()) existingProfile.setNickname(nickname);
      if (versiculo != null && !versiculo.isBlank()) existingProfile.setVersiculo(versiculo);
      if (igreja != null && !igreja.isBlank()) existingProfile.setIgreja(igreja);
      if (ministerio != null && !ministerio.isBlank()) existingProfile.setMinisterio(ministerio);
      if (interesses != null && !interesses.isBlank()) existingProfile.setInteresses(interesses);
      if (oracao != null && !oracao.isBlank()) existingProfile.setOracao(oracao);

      if (avatarFile != null && !avatarFile.isEmpty())
        existingProfile.setAvatar(avatarFile.getBytes());
      if (imagePrincipalFile != null && !imagePrincipalFile.isEmpty())
        existingProfile.setImagePrincipal(imagePrincipalFile.getBytes());

      profileService.saveProfile(existingProfile);

    } catch (IOException e) {
      e.printStackTrace();
      return "redirect:/profile?error=uploadError";
    }

    return "redirect:/profile";
  }
}
