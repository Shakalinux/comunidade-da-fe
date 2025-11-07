package com.shakalinux.biblia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VersiculoSalvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_salvo", nullable = false)
    private LocalDateTime dataSalvo = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "versiculo_id", nullable = false)
    @JsonIgnore
    private Versiculo versiculo;

    @Lob
    @Column(name = "reflexao", columnDefinition = "TEXT")
    private String reflexao;

    @Column(unique = true)
    private String uniqueKey;

    @PrePersist
    @PreUpdate
    private void generateUniqueKey() {
        if (user != null && versiculo != null) {
            this.uniqueKey = user.getId() + "_" + versiculo.getId();
        }
    }
}
