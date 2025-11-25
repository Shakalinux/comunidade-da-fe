package com.shakalinux.biblia.service;


import com.shakalinux.biblia.model.Post;
import com.shakalinux.biblia.model.PostLike;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.repository.PostLikeRepository;
import com.shakalinux.biblia.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  PostLikeService{
    @Autowired
    private  PostLikeRepository likeRepository;
    @Autowired
    private  PostRepository postRepository;

    @Transactional
    public int toggleLike(User user, Post post) {

        boolean jaCurtiu = likeRepository.existsByUserAndPost(user, post);

        if (jaCurtiu) {
            likeRepository.deleteByUserAndPost(user, post);
        } else {
            likeRepository.save(new PostLike(null, user, post));
        }

        return likeRepository.countByPost(post);
    }
    public boolean jaCurtiu(User user, Post post) {
        return likeRepository.existsByUserAndPost(user, post);
    }




}
