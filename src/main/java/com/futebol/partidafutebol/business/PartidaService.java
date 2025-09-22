package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.PartidaDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final ClubeService clubeService;
    private final EstadioService estadioService;

    // 1. Cadastra uma partida
    @Transactional
    public PartidaDto cadastrarPartida(PartidaDto partidaDto) {
        // I. Validação clubes devem ser diferentes
        if (partidaDto.getClubeMandanteId().equals(partidaDto.getClubeVisitanteId())) {
            throw new IllegalArgumentException("Clubes mandante e visitante devem ser diferentes");
        }
        // II. Criar a partida
        Partida partida = Partida.builder()
                .clubeMandante(clubeService.findById(partidaDto.getClubeMandanteId()))
                .clubeVisitante(clubeService.findById(partidaDto.getClubeVisitanteId()))
                .estadioPartida(estadioService.findById(partidaDto.getEstadioPartidaId()))
                .dataHora(partidaDto.getDataHora())
                .resultado(partidaDto.getResultado())
                .build();
        // III. Salvar no banco
        Partida partidaSalva = partidaRepository.save(partida);
        // IV. Retornar DTO com dados salvos
        return new PartidaDto(
                partidaSalva.getClubeMandante().getId(),
                partidaSalva.getClubeVisitante().getId(),
                partidaSalva.getDataHora(),
                partidaSalva.getEstadioPartida().getId(),
                partidaSalva.getResultado()
        );
    }
    // 2. Editar partida
    @Transactional
    public PartidaDto editarPartida(PartidaDto partidaDto, Integer id) {
        // I. Validar se partida existe
        Partida partidaEntity = findById(id);
        // II. Atualizar os dados da partida
        Partida partidaAtualizada = Partida.builder()
                .id(partidaEntity.getId())
                .clubeMandante(partidaDto.getClubeMandanteId() != null ? clubeService.findById(partidaDto.getClubeMandanteId()) : partidaEntity.getClubeMandante())
                .clubeVisitante(partidaDto.getClubeVisitanteId() != null ? clubeService.findById(partidaDto.getClubeVisitanteId()) : partidaEntity.getClubeVisitante())
                .estadioPartida(partidaDto.getEstadioPartidaId() != null ? estadioService.findById(partidaDto.getEstadioPartidaId()) : partidaEntity.getEstadioPartida())
                .dataHora(partidaDto.getDataHora() != null ? partidaDto.getDataHora() : partidaEntity.getDataHora())
                .resultado(partidaDto.getResultado() != null ? partidaDto.getResultado() : partidaEntity.getResultado())
                .build();

        // III. Salvar no banco
        Partida partidaSalva = partidaRepository.save(partidaAtualizada);

        // IV. Retornar DTO
        return new PartidaDto(
                partidaSalva.getClubeMandante().getId(),
                partidaSalva.getClubeVisitante().getId(),
                partidaSalva.getDataHora(),
                partidaSalva.getEstadioPartida().getId(),
                partidaSalva.getResultado()
        );
    }
    // 3. Remover partida
    @Transactional
    public PartidaDto excluirPartida(Integer id) {

        // I. Validar se a partida existe
        Partida partidaEntity = findById(id);

        // II. Remover a partida
        partidaRepository.deleteById(partidaEntity.getId());

        // III. Retornar DTO
        return new PartidaDto(
                partidaEntity.getClubeMandante().getId(),
                partidaEntity.getClubeVisitante().getId(),
                partidaEntity.getDataHora(),
                partidaEntity.getEstadioPartida().getId(),
                partidaEntity.getResultado()
        );
    }
    // 4. Buscar partida por Id
    public PartidaDto buscarPartidaPorId(Integer id) {
        // I. Validar se a partida existe
        Partida partidaEntity = findById(id);

        // II. Retornar DTO
        return new PartidaDto(
                partidaEntity.getClubeMandante().getId(),
                partidaEntity.getClubeVisitante().getId(),
                partidaEntity.getDataHora(),
                partidaEntity.getEstadioPartida().getId(),
                partidaEntity.getResultado()
        );
    }
    // 5. Listar todas as partidas
    public List<PartidaDto> listarTodasPartidas(Integer clubeMandanteId, Integer clubeVisitanteId, Integer estadioPartidaId, LocalDateTime dataHora, String resultado) {

        // I. Buscar todas as partidas
        List<Partida> partidas = partidaRepository.findAll();
        // II. Aplicar filtros usando IDs
        return partidas.stream()
                .filter(partida -> clubeMandanteId == null || partida.getClubeMandante().getId().equals(clubeMandanteId))
                .filter(partida -> clubeVisitanteId == null || partida.getClubeVisitante().getId().equals(clubeVisitanteId))
                .filter(partida -> estadioPartidaId == null || partida.getEstadioPartida().getId().equals(estadioPartidaId))
                .filter(partida -> dataHora == null || partida.getDataHora().equals(dataHora))
                .filter(partida -> resultado == null || partida.getResultado().equals(resultado))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    // Método auxiliar para converter Partida em DTO
    private PartidaDto convertToDto(Partida partida) {
        return new PartidaDto(
                partida.getClubeMandante().getId(),
                partida.getClubeVisitante().getId(),
                partida.getDataHora(),
                partida.getEstadioPartida().getId(),
                partida.getResultado()
        );
    }

    // Método genérico para buscar por ID
    public Partida findById(Integer id) {
        Partida partidaEntity = partidaRepository.findById(id).orElseThrow(
                ()  -> new RuntimeException("Partida inexistente"));
        return partidaEntity;
    }
}


