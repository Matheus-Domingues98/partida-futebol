package com.futebol.partidafutebol.business;


import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private ClubeRepository clubeRepository;

    public List<Clube> listarTodosClubes() {
        return clubeRepository.findAll();
    }

    public Clube salvarClube(Clube clube) {
        return clubeRepository.saveAndFlush(clube);
    }

    public Clube buscarClubePorNome(String nome) {
        return clubeRepository.findByNome(nome).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
    }

     public void atualizarClubePorId(Integer id, Clube clube) {
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
        Clube clubeAtualizado = Clube.builder()
                .nome(clube.getNome() != null ? clube.getNome() : clubeEntity.getNome())
                .id(clubeEntity.getId())
                .build();

        clubeRepository.saveAndFlush(clubeAtualizado);
     }

    public void deletarClubePorNome(String nome) {
        clubeRepository.deleteByNome(nome);
    }

    public Clube inativarClubePorId(Integer id) {
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
        clubeEntity.setAtivo(false);

        return  clubeRepository.save(clubeEntity);
    }

    public Clube ativarClubePorId(Integer id) {
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
        clubeEntity.setAtivo(true);

        return clubeRepository.save(clubeEntity);
    }
}




