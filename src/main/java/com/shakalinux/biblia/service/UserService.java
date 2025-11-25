package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> searchUserId(Long id) {
        return userRepository.findById(id);
    }


    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            User user = userRepository.findByUsername(username);

            if (user != null && user.getProfile() != null) {
                profileService.encodeImages(user.getProfile());
            }

            return user;
        }

        return null;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getProfile() != null) {
            profileService.encodeImages(user.getProfile());
        }
        return user;
    }
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getProfile() != null) {
            profileService.encodeImages(user.getProfile());
        }
        return user;
    }
    public List<User> listAllUsersWithEncodedProfiles() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getProfile() != null) {
                profileService.encodeImages(user.getProfile());
            }
        }

        return users;
    }


}
