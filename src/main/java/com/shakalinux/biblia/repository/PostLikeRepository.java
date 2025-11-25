package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Post;
import com.shakalinux.biblia.model.PostLike;
import com.shakalinux.biblia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);

    int countByPost(Post post);

}
