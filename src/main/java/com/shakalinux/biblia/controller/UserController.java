package com.shakalinux.biblia.controller;

import com.shakalinux.biblia.model.Profile;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.service.EmailService;
import com.shakalinux.biblia.service.ProfileService;
import com.shakalinux.biblia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/cadastro")
    public String getCadastre(Model model) {
        model.addAttribute("user", new User());
        return "user/cadastro";
    }

    @PostMapping("/cadastro")
    public String registerUser(@ModelAttribute @Validated User user,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) return "user/cadastro";

        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "Nome de usuário já existe!");
            return "redirect:/user/cadastro";
        }

        Profile profile = new Profile();
        profileService.saveProfile(profile);
        user.setProfile(profile);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);

        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        user.setVerificationCode(code);
        userService.saveUser(user);

        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), code);
        redirectAttributes.addFlashAttribute("message", "Usuário cadastrado! Verifique seu e-mail.");
        return "redirect:/user/verify?username=" + user.getUsername();
    }


    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @PostMapping("/forgot-password")
    @ResponseBody
    public Map<String, Object> sendResetCodeAjax(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.findByEmail(email);
        if (user == null) {
            response.put("status", "error");
            response.put("message", "E-mail não encontrado.");
            return response;
        }

        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        user.setVerificationCode(code);
        userService.saveUser(user);

        emailService.sendResetPasswordEmail(user.getEmail(), user.getUsername(), code);

        response.put("status", "success");
        response.put("username", user.getUsername());
        response.put("message", "Código enviado com sucesso! Verifique seu e-mail e insira abaixo.");
        return response;
    }

    @PostMapping("/reset-password-modal")
    @ResponseBody
    public Map<String, Object> resetPasswordModal(@RequestParam String username,
                                                  @RequestParam String code,
                                                  @RequestParam String password,
                                                  @RequestParam String confirm) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsername(username);

        if (user == null) {
            response.put("status", "error");
            response.put("message", "Usuário não encontrado.");
            return response;
        }

        if (!code.equals(user.getVerificationCode())) {
            response.put("status", "error");
            response.put("message", "Código inválido ou expirado.");
            return response;
        }

        if (!password.equals(confirm)) {
            response.put("status", "error");
            response.put("message", "As senhas não coincidem.");
            return response;
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setVerificationCode(null);
        userService.saveUser(user);

        response.put("status", "success");
        response.put("message", "Senha redefinida com sucesso! Faça login.");
        return response;
    }


    @GetMapping("/verify")
    public String getVerify(@RequestParam String username, Model model) {
        User user = userService.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Usuário não encontrado.");
            return "user/login";
        }

        if (user.isVerified()) {
            model.addAttribute("success", "Conta já verificada! Faça login.");
            return "user/login";
        }

        model.addAttribute("username", username);
        return "user/verify/verificador";
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam String username,
                             @RequestParam String code,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        User user = userService.findByUsername(username);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado.");
            return "redirect:/user/login";
        }

        if (code.equals(user.getVerificationCode())) {
            user.setVerified(true);
            user.setVerificationCode(null);
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "Conta verificada com sucesso! Faça login.");
            return "redirect:/user/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("error", "Código inválido!");
        return "user/verify/verificador";
    }
}
