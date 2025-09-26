package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.PartidaDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import com.futebol.partidafutebol.infrastructure.repository.EstadioRepository;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final ClubeService clubeService;
    private final EstadioService estadioService;
    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;

    // 1. Cadastra uma partida
    @Transactional
    public PartidaDto cadastrarPartida(PartidaDto partidaDto) {

        // I. Validar dados da partida
        validarPartida(partidaDto);

        // III. Criar a partida
        Partida partida = Partida.builder()
                .clubeMandante(clubeService.findById(partidaDto.getClubeMandanteId()))
                .clubeVisitante(clubeService.findById(partidaDto.getClubeVisitanteId()))
                .estadioPartida(estadioService.findById(partidaDto.getEstadioPartidaId()))
                .dataHora(partidaDto.getDataHora())
                .resultado(partidaDto.getResultado())
                .build();
        // IV. Salvar no banco
        Partida partidaSalva = partidaRepository.save(partida);
        // V. Retornar DTO com dados salvos
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
        // II. Validar dados da partida
        validarPartida(partidaDto);
        // III. Atualizar os dados da partida
        Partida partidaAtualizada = Partida.builder()
                .id(partidaEntity.getId())
                .clubeMandante(partidaDto.getClubeMandanteId() != null ? clubeService.findById(partidaDto.getClubeMandanteId()) : partidaEntity.getClubeMandante())
                .clubeVisitante(partidaDto.getClubeVisitanteId() != null ? clubeService.findById(partidaDto.getClubeVisitanteId()) : partidaEntity.getClubeVisitante())
                .estadioPartida(partidaDto.getEstadioPartidaId() != null ? estadioService.findById(partidaDto.getEstadioPartidaId()) : partidaEntity.getEstadioPartida())
                .dataHora(partidaDto.getDataHora() != null ? partidaDto.getDataHora() : partidaEntity.getDataHora())
                .resultado(partidaDto.getResultado() != null ? partidaDto.getResultado() : partidaEntity.getResultado())
                .build();

        // IV. Salvar no banco
        Partida partidaSalva = partidaRepository.save(partidaAtualizada);

        // V. Retornar DTO
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

    // Método para buscar por ID
    public Partida findById(Integer id) {
        return partidaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida inexistente"));
    }

    public PartidaDto validarPartida(PartidaDto partidaDto) {
        // validar dados minimos mencionados
        if (partidaDto.getClubeMandanteId().equals(partidaDto.getClubeVisitanteId()) || partidaDto.getClubeMandanteId() == null || partidaDto.getClubeVisitanteId() == null
                || partidaDto.getEstadioPartidaId() == null || partidaDto.getDataHora() == null || partidaDto.getResultado() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Partida inexistente");
        }

        // Validar se a data da partida é posterior à data de criação dos clubes
        validarDataPartidaPosteriorCriacaoClubes(partidaDto);

        // Validar se clube esta ativo
        validarClubeAtivo(partidaDto);

        // Partidas com horarios proximos
        validarConflitoDeHorario(partidaDto.getDataHora());

        // Validar se estadio ja possui jogo no mesmo horario
        validarConflitoEstadio(partidaDto);

        return partidaDto;
    }

    // Valida se a data da partida é posterior à data de criação dos clubes envolvidos
    private void validarDataPartidaPosteriorCriacaoClubes(PartidaDto partidaDto) {

        // Buscar os clubes mandante e visitante
        Clube clubeMandante = clubeService.findById(partidaDto.getClubeMandanteId());
        Clube clubeVisitante = clubeService.findById(partidaDto.getClubeVisitanteId());

        // Converter LocalDateTime da partida para LocalDate para comparação
        LocalDate dataPartida = partidaDto.getDataHora().toLocalDate();

        // Validar clube mandante
        if (clubeMandante.getDataCriacao() != null && dataPartida.isBefore(clubeMandante.getDataCriacao())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data da partida deve ser posterior à data de criação do clube mandante");
        }
        // Validar clube visitante
        if (clubeVisitante.getDataCriacao() != null && dataPartida.isBefore(clubeVisitante.getDataCriacao())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data da partida deve ser posterior à data de criação do clube visitante");
        }
    }

    private void validarClubeAtivo(PartidaDto partidaDto) {
        // Buscar os clubes mandante e visitante
        Clube clubeMandante = clubeRepository.findById(partidaDto.getClubeMandanteId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube mandante inexistente"));
        Clube clubeVisitante = clubeRepository.findById(partidaDto.getClubeVisitanteId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube visitante inexistente"));

        // Validar clube mandante
        if (!clubeMandante.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube mandante deve estar ativo");
        }
        // Validar clube visitante
        if (!clubeVisitante.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube visitante deve estar ativo");
        }
    }


    private void validarConflitoDeHorario(LocalDateTime dataHoraNovaPartida) {
        LocalDateTime inicio = dataHoraNovaPartida.minusHours(48);
        LocalDateTime fim = dataHoraNovaPartida.plusHours(48);

        List<Partida> partidasProximas = partidaRepository.findByDataHoraBetween(inicio, fim);

        if (!partidasProximas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma partida marcada em menos de 48 horas deste horário.");
        }
    }

    // Valida se o estádio já possui uma partida no mesmo horário
    private void validarConflitoEstadio(PartidaDto partidaDto) {
        LocalDateTime dataHoraPartida = partidaDto.getDataHora();
        Integer estadioId = partidaDto.getEstadioPartidaId();
        
        // Buscar partidas no mesmo estádio no mesmo dia
        List<Partida> partidasNoEstadio = partidaRepository.findByEstadioPartidaIdAndDataHoraBetween(
            estadioId,
            dataHoraPartida.toLocalDate().atStartOfDay(),
            dataHoraPartida.toLocalDate().atTime(23, 59, 59)
        );
        
        // Verificar se há conflito de horário (margem de 3 horas)
        for (Partida partida : partidasNoEstadio) {
            long horasEntre = Math.abs(java.time.Duration.between(partida.getDataHora(), dataHoraPartida).toHours());
            if (horasEntre < 3) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "O estádio já possui uma partida agendada em menos de 3 horas deste horário");
            }
        }
    }
}



