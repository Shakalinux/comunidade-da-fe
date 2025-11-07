package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.Acess;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.repository.AcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AcessService {

    @Autowired
    private AcessRepository acessRepository;

    @Autowired
    private BibliaService bibliaService;

    public Acess save(Acess acess) {
        return acessRepository.save(acess);
    }


    public List<Acess> findByUser(User user) {
        return acessRepository.findByUser(user);
    }

    public void registrarAcessoDiario(User user) {
        bibliaService.registrarAcessoDiario(user);
    }

    public List<Acess> findByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return acessRepository.findByUserAndDataAcessoBetween(user, startDate, endDate);
    }


    public long countDiasAcessados(User user) {
        return acessRepository.findByUser(user)
            .stream()
            .map(Acess::getDataAcesso)
            .distinct()
            .count();
    }
}
