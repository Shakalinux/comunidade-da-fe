package com.shakalinux.biblia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "versiculos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Immutable
@ToString(exclude = {"livro", "versao"})
public class Versiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ver_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ver_liv_id", referencedColumnName = "liv_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "versiculos"})
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ver_vrs_id", referencedColumnName = "vrs_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "versiculos"})
    private Versao versao;


    @Column(name = "ver_capitulo", nullable = false)
    private Integer capitulo;

    @Column(name = "ver_versiculo", nullable = false)
    private Integer numero;

    @Column(name = "ver_texto", columnDefinition = "TEXT", nullable = false)
    private String texto;



}
