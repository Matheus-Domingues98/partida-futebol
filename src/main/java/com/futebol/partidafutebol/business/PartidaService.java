package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.PartidaDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import com.futebol.partidafutebol.infrastructure.repository.EstadioRepository;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;

    public Partida salvarPartida(Partida partida) {
        return partidaRepository.saveAndFlush(partida);
    }

    public Partida salvarPartidaPorNomes(PartidaCadastroDto partidaDto) {
        // Busca clube mandante por nome
        Clube clubeMandante = clubeRepository.findByNome(partidaDto.getClubeMandanteNome())
                .orElseThrow(() -> new RuntimeException("Clube mandante não encontrado: " + partidaDto.getClubeMandanteNome()));
        
        // Busca clube visitante por nome
        Clube clubeVisitante = clubeRepository.findByNome(partidaDto.getClubeVisitanteNome())
                .orElseThrow(() -> new RuntimeException("Clube visitante não encontrado: " + partidaDto.getClubeVisitanteNome()));
        
        // Busca estádio por nome
        Estadio estadio = estadioRepository.findByNome(partidaDto.getEstadioNome())
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado: " + partidaDto.getEstadioNome()));
        
        // Validação: clubes devem ser diferentes
        if (clubeMandante.equals(clubeVisitante)) {
            throw new IllegalArgumentException("Clubes devem ser diferentes");
        }
        
        // Cria a partida
        Partida partida = Partida.builder()
                .clubeMandante(clubeMandante)
                .clubeVisitante(clubeVisitante)
                .estadioPartida(estadio)
                .dataHora(partidaDto.getDataHora())
                .resultado(partidaDto.getResultado())
                .build();
        
        return partidaRepository.saveAndFlush(partida);
    }

    public List<Partida> listarTodasPartidas() {
        return partidaRepository.findAll();
    }

    public void deletarPartidaPorId(Integer id) {
        partidaRepository.deleteById(id);
    }

    public Partida buscarPartidaPorId(Integer id) {
        return partidaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Partida nao encontrada")
        );
    }

    @Transactional
    public void atualizarPartidaPorId(Integer id, PartidaDto partida) {
        Partida partidaEntity = partidaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Partida não encontrada")
        );
        Partida partidaAtualizada = Partida.builder()
                .id(partidaEntity.getId())
                .clubeMandante(partida.getClubeMandante() != null ? partida.getClubeMandante() : partidaEntity.getClubeMandante())
                .clubeVisitante(partida.getClubeVisitante() != null ? partida.getClubeVisitante() : partidaEntity.getClubeVisitante())
                .dataHora(partida.getDataHora() != null ? partida.getDataHora() : partidaEntity.getDataHora())
                .resultado(partida.getResultado() != null ? partida.getResultado() : partidaEntity.getResultado())
                .estadioPartida(partida.getEstadioPartida() != null ? partida.getEstadioPartida() : partidaEntity.getEstadioPartida())
                .build();

        partidaRepository.saveAndFlush(partidaAtualizada);
    }


}
