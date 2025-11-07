package com.shakalinux.biblia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "versoes")
public class Versao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrs_id")
    private Long id;

    @Column(name = "vrs_nome", nullable = false, length = 150)
    private String nome;
}
