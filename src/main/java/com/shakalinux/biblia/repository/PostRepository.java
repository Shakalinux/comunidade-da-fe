package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    Long countByUserId(Long id);
}
