package com.shakalinux.biblia.security;

import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByUsername(username);

        if (appUser == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        if (!appUser.isVerified()) {
            throw new UsernameNotFoundException("Conta não verificada. Verifique seu e-mail antes de acessar.");
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(appUser.getUsername())
            .password(appUser.getPassword())
            .roles("USER")
            .disabled(!appUser.isVerified())
            .build();
    }
}
