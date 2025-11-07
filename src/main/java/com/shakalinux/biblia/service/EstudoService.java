package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.Estudo;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.repository.EstudoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EstudoService {

    @Autowired
    private IaMensagemService iaService;

    @Autowired
    private EstudoRepository estudoRepository;

    private final List<String> temasDevocional = Arrays.asList(
        "Gratidão",
        "Paz interior",
        "Força na adversidade",
        "Amor ao próximo",
        "Sabedoria divina",
        "Perdão",
        "Esperança eterna",
        "Fé inabalável",
        "Confiança em Deus",
        "Vida em santidade",
        "Humildade",
        "Obediência",
        "Oração e intimidade com Deus",
        "Propósito de vida",
        "Esperar o tempo de Deus",
        "Renovação espiritual",
        "Alegria no Senhor",
        "Vencer o medo",
        "Descanso em Deus",
        "Andar pela fé",
        "Serviço e compaixão",
        "Discernimento espiritual",
        "Coragem diante das provações",
        "Amor de Deus",
        "Transformação do coração",
        "Aliança com Deus",
        "Vitória espiritual",
        "Paciência e perseverança",
        "Esperança no futuro",
        "Reconhecimento da graça",
        "Luz em meio às trevas"
    );


    public List<String> getTemas() {
        return temasDevocional;
    }


    public String gerarEstudos() {
        Random random = new Random();
        return temasDevocional.get(random.nextInt(temasDevocional.size()));
    }


    public String gerarEstudo(String tema) {
        return iaService.gerarMensagemBiblica(tema);
    }


    public List<Estudo> findEstudos(User user) {
        return estudoRepository.findByUser(user);
    }


    public Estudo save(Estudo estudo) {
        if (estudo == null) {
            return null;
        }
        return estudoRepository.save(estudo);
    }
}
