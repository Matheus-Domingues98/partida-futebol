package com.futebol.partidafutebol.business;


import com.futebol.partidafutebol.dto.ClubeDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public List<Clube> listarTodosClubes() {
        return clubeRepository.findAll();
    }

    public List<Clube> listarClubesComFiltros(String nome, String uf, Boolean ativo) {
        // Se nenhum filtro foi fornecido, retorna todos
        if (nome == null && uf == null && ativo == null) {
            return clubeRepository.findAll();
        }
        
        if (nome != null) {
            return clubeRepository.findByNomeContainingIgnoreCase(nome);
        }
        
        if (uf != null) {
            return clubeRepository.findByUf(uf);
        }
        
        if (ativo != null) {
            return ativo ? clubeRepository.findByAtivoTrue() : clubeRepository.findByAtivoFalse();
        }
        
        return clubeRepository.findAll();
    }

    public Clube salvarClube(Clube clube) {
        return clubeRepository.saveAndFlush(clube);
    }

    public Clube buscarClubePorId(Integer id) {
        return clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
    }

     @Transactional
     public void atualizarClubePorId(Integer id, ClubeDto clube) {
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
        Clube clubeAtualizado = Clube.builder()
                .id(clubeEntity.getId())
                .nome(clube.getNome() != null ? clube.getNome() : clubeEntity.getNome())
                .uf(clube.getUf() != null ? clube.getUf() : clubeEntity.getUf())
                .dataCriacao(clube.getDataCriacao() != null ? clube.getDataCriacao() : clubeEntity.getDataCriacao())
                .ativo(clube.isAtivo() || clubeEntity.isAtivo())
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

    /*
    public Clube ativarClubePorId(Integer id) {
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));
        clubeEntity.setAtivo(true);

        return clubeRepository.save(clubeEntity);
    }

     */
}




