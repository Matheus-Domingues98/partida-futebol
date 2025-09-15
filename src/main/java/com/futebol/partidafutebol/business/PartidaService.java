package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {
    @Autowired
    private PartidaRepository partidaRepository;

    public Partida salvarPartida(Partida partida) {
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

    public void atualizarPartidaPorId(Integer id, Partida partida) {
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
