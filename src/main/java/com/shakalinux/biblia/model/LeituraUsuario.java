package com.shakalinux.biblia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "leitura_usuario")
public class LeituraUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(name = "capitulo_atual")
    private Integer capituloAtual;

    @Column(name = "total_capitulos")
    private Integer totalCapitulos;

    @Column(name = "progresso_percentual")
    private Integer progressoPercentual;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    public LeituraUsuario(User user, Livro livro, Integer capituloAtual, Integer totalCapitulos) {
        this.user = user;
        this.livro = livro;
        this.capituloAtual = capituloAtual;
        this.totalCapitulos = totalCapitulos;
        this.progressoPercentual = (int) Math.round((capituloAtual * 100.0) / totalCapitulos);
        this.ultimaAtualizacao = LocalDateTime.now();
    }
}
