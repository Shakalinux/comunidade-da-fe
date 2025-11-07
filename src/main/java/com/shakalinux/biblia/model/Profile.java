package com.shakalinux.biblia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfil;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nickname;

    private String frase;

    @Lob
    private byte[] avatar;

    @Lob
    private byte[] imagePrincipal;

    @Transient
    private MultipartFile avatarFile;

    @Transient
    private MultipartFile imagePrincipalFile;

    @Column(length = 100)
    private String versiculo;

    @Column(length = 100)
    private String igreja;

    @Column(length = 100)
    private String ministerio;

    @Column(length = 200)
    private String interesses;

    @Column(length = 200)
    private String oracao;


    @Transient
    private String avatar64;
    @Transient
    private String imagePrincipal64;


    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private User user;
}
