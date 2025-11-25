package com.shakalinux.biblia.controller;


import com.shakalinux.biblia.model.Message;
import com.shakalinux.biblia.model.Post;
import com.shakalinux.biblia.model.Profile;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.service.*;
import com.shakalinux.biblia.utils.WebSocketEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comunidade")
@PreAuthorize("isAuthenticated()")
public class ComunidadeChat {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WebSocketEventListener wsListener;

    @Autowired
    private PostService postService;

    @Autowired
    private PostLikeService  postLikeService;

    @GetMapping("/chat-online")
    public String chatOnline(Model model, Principal principal) {
        String loggedInUsername = principal.getName();

        List<User> allUsers = userService.listAllUsersWithEncodedProfiles();

        List<User> usersForDisplay = allUsers.stream()
                .filter(user -> !user.getUsername().equals(loggedInUsername))
                .collect(Collectors.toList());

        long unreadMessagesCount = messageService.countUnreadMessages(loggedInUsername);
        model.addAttribute("unreadMessagesCount", unreadMessagesCount);


        Map<String, Long> unreadCounts = new HashMap<>();
        for (User user : usersForDisplay) {
            long count = messageService.countUnreadMessagesFromSender(loggedInUsername, user.getUsername());
            if (count > 0) {
                unreadCounts.put(user.getUsername(), count);
            }
        }
        model.addAttribute("unreadCounts", unreadCounts);

        model.addAttribute("users", usersForDisplay);

        User userAuth = userService.getLoggedInUser();
        model.addAttribute("profile", userAuth.getProfile());
        model.addAttribute("onlineUsers", wsListener.getOnlineUsers());

        return "chat-online";
    }

    @GetMapping("/chat/privado/{username}")
    public String chatPrivado(@PathVariable String username,
                              Principal principal,
                              Model model) {

        String sender = principal.getName();
        String receiver = username.trim();



        messageService.markMessagesAsRead(sender, receiver);

        List<Message> messages = messageService.findChatMessagesBetween(sender, receiver);

        User userAuth = userService.getLoggedInUser();
        User destinatario = userService.findByUsername(receiver);

        model.addAttribute("messages", messages);

        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);

        model.addAttribute("profile", userAuth.getProfile());
        model.addAttribute("destinatario", destinatario);

        model.addAttribute("onlineUsers", wsListener.getOnlineUsers());

        return "chat-privado";
    }

    @GetMapping("/feed")
    public String feed(Model model) {

        User user = userService.getLoggedInUser();

        List<Post> posts = postService.listarTodos();

        for (Post post : posts) {
            boolean liked = postLikeService.jaCurtiu(user, post);
            post.setLikedByUser(liked);

            Profile authorProfile = post.getUser().getProfile();
            profileService.encodeImages(authorProfile);
        }
        profileService.encodeImages(user.getProfile());

        model.addAttribute("posts", posts);
        model.addAttribute("profile", user.getProfile());

        return "feed";
    }



    @GetMapping("/novo")
    public String newPost(Model model) {
        User user = userService.getLoggedInUser();
        Profile profile = user.getProfile();
        model.addAttribute("profile", profile);
        return "novaPublicacao";
    }
    @PostMapping("/salvar")
    public String salvar(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam("attachment") MultipartFile file,
            Principal principal
    ) throws Exception {
        User user = userService.findByUsername(principal.getName());

        postService.criarPost(title, content, file, user);

        return "redirect:/comunidade/feed";
    }

    @GetMapping("/{id}")
    public String ver(@PathVariable Long id, Model model) {
        User user = userService.getLoggedInUser();


        Post post = postService.buscarPorId(id);
        Profile profile = post.getUser().getProfile();
        profileService.encodeImages(profile);


        if (post == null) {
            return "redirect:/comunidade/feed";
        }

        model.addAttribute("post", post);
        model.addAttribute("profile", profile);
        model.addAttribute("profileAuth", user.getProfile());
        return "verPublicacao";
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> arquivo(@PathVariable Long id) {
        Post post = postService.buscarPorId(id);

        if (post == null || post.getAttachment() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(post.getAttachment());
    }

    @PostMapping("/like/{id}")
    @ResponseBody
    public Map<String, Object> darLike(@PathVariable Long id) {

        User user = userService.getLoggedInUser();
        Post post = postService.buscarPorId(id);

        boolean jaCurtiuAntes = postLikeService.jaCurtiu(user, post);

        int likesAtualizados = postLikeService.toggleLike(user, post);

        boolean curtiuAgora = !jaCurtiuAntes;

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("likes", likesAtualizados);
        resposta.put("curtiu", curtiuAgora);

        return resposta;
    }



}