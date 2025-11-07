package com.shakalinux.biblia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Table(name = "testamentos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Immutable
public class Testamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tes_id")
    private Integer id;

    @Column(name = "tes_nome", length = 30, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "testamento", fetch = FetchType.LAZY)
    private List<Livro> livros;
}
