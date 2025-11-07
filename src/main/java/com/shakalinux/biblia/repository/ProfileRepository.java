package com.shakalinux.biblia.repository;

import com.shakalinux.biblia.model.Profile;
import com.shakalinux.biblia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);
}
