package com.shakalinux.biblia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Table(name = "livros")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Immutable
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liv_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liv_tes_id", referencedColumnName = "tes_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "livros"})
    private Testamento testamento;

    @Column(name = "liv_posicao", nullable = false)
    private Integer posicao;

    @Column(name = "liv_nome", length = 30, nullable = false)
    private String nome;

    @Column(name = "liv_abreviado", length = 3)
    private String abreviado;

    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Versiculo> versiculos;
}
