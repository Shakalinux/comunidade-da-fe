package com.shakalinux.biblia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/biblia/**",
                    "/static/**",
                    "/user/cadastro",
                    "/user/login",
                    "/user/verify/**",
                    "/user/resend-verification",
                    "/user/forgot-password",
                    "/user/reset-password",
                    "/user/reset-password-modal"
                ).permitAll()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/user/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/user/login?logout")
                .permitAll()
            )
            .userDetailsService(userDetailsServiceImpl);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
