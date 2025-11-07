package com.shakalinux.biblia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String username;

    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    @Size(max = 50, message = "O email deve ter no máximo 50 caracteres")
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "idPerfil")
    private Profile profile;

    @Column(name = "verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean verified = false;

    @Column(name = "verification_code", length = 6, columnDefinition = "VARCHAR(6)")
    private String verificationCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VersiculoSalvo> versiculosSalvos;


}
