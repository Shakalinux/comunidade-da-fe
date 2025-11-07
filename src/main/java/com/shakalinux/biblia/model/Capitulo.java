package com.shakalinux.biblia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Table(name = "capitulos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Immutable
public class Capitulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "total_versiculos", nullable = false)
    private Integer totalVersiculos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", referencedColumnName = "liv_id", nullable = false)
    private Livro livro;

    @OneToMany(mappedBy = "capitulo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Versiculo> versiculos;
}
