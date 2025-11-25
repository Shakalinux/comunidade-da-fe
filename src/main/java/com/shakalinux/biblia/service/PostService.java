package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.Post;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> listarTodos() {
        return postRepository.findAll();
    }

    public Post buscarPorId(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post criarPost(String title, String content, MultipartFile file, User user) throws IOException {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);

        if (file != null && !file.isEmpty()) {
            post.setAttachment(file.getBytes());
        }

        return postRepository.save(post);
    }

    public int contarPostagensDoUsuario(Long userId) {
        return Math.toIntExact(postRepository.countByUserId(userId));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
