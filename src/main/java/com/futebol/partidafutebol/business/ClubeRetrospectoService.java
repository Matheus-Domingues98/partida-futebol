package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.ClubeRetrospectoDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.ClubeRetrospecto;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRetrospectoRepository;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubeRetrospectoService {

    private final ClubeRetrospectoRepository clubeRetrospectoRepository;
    private final ClubeService clubeService;
    private final PartidaService partidaService;
    private final PartidaRepository partidaRepository;

    public ClubeRetrospectoDto atualizarTotalVitorias(Integer clubeId, Integer totalVitorias) {
        // I. Validar se clube existe
        Clube clube = clubeService.findById(clubeId);

        // II. Buscar retrospecto existente ou criar novo
        ClubeRetrospecto retrospecto = clubeRetrospectoRepository.findByClubeId(clubeId);

        if (retrospecto == null) {
            // Criar novo retrospecto se não existir
            retrospecto = ClubeRetrospecto.builder()
                    .clube(clube)
                    .totalVitorias(totalVitorias)
                    .totalEmpates(0)
                    .totalDerrotas(0)
                    .totalGolsMarcados(0)
                    .totalGolsSofridos(0)
                    .build();
        } else {
            // Atualizar retrospecto existente
            retrospecto.setTotalVitorias(totalVitorias);
        }

        // III. Salvar no banco de dados
        ClubeRetrospecto retrospectoSalvo = clubeRetrospectoRepository.save(retrospecto);

        // IV. Retornar DTO com dados atualizados
        return ClubeRetrospectoDto.builder()
                .clubeId(retrospectoSalvo.getClube().getId())
                .nomeClube(retrospectoSalvo.getClube().getNome())
                .totalVitorias(retrospectoSalvo.getTotalVitorias())
                .totalEmpates(retrospectoSalvo.getTotalEmpates())
                .totalDerrotas(retrospectoSalvo.getTotalDerrotas())
                .totalGolsMarcados(retrospectoSalvo.getTotalGolsMarcados())
                .totalGolsSofridos(retrospectoSalvo.getTotalGolsSofridos())
                .build();
    }

    public ClubeRetrospectoDto atualizarTotalEmpates(Integer clubeId, Integer totalEmpates) {
        // I. Validar se clube existe
        Clube clube = clubeService.findById(clubeId);

        // II. Buscar retrospecto existente ou criar novo
        ClubeRetrospecto retrospecto = clubeRetrospectoRepository.findByClubeId(clubeId);

        if (retrospecto == null) {
            // Criar novo retrospecto se não existir
            retrospecto = ClubeRetrospecto.builder()
                    .clube(clube)
                    .totalVitorias(0)
                    .totalEmpates(totalEmpates)
                    .totalDerrotas(0)
                    .totalGolsMarcados(0)
                    .totalGolsSofridos(0)
                    .build();
        } else {
            // Atualizar retrospecto existente
            retrospecto.setTotalEmpates(totalEmpates);
        }

        // III. Salvar no banco de dados
        ClubeRetrospecto retrospectoSalvo = clubeRetrospectoRepository.save(retrospecto);

        // IV. Retornar DTO com dados atualizados
        return ClubeRetrospectoDto.builder()
                .clubeId(retrospectoSalvo.getClube().getId())
                .nomeClube(retrospectoSalvo.getClube().getNome())
                .totalVitorias(retrospectoSalvo.getTotalVitorias())
                .totalEmpates(retrospectoSalvo.getTotalEmpates())
                .totalDerrotas(retrospectoSalvo.getTotalDerrotas())
                .totalGolsMarcados(retrospectoSalvo.getTotalGolsMarcados())
                .totalGolsSofridos(retrospectoSalvo.getTotalGolsSofridos())
                .build();
    }

    public ClubeRetrospectoDto atualizarTotalDerrotas(Integer clubeId, Integer totalDerrotas) {
        // I. Validar se clube existe
        Clube clube = clubeService.findById(clubeId);
        
        // II. Buscar retrospecto existente ou criar novo
        ClubeRetrospecto retrospecto = clubeRetrospectoRepository.findByClubeId(clubeId);
        
        if (retrospecto == null) {
            // Criar novo retrospecto se não existir
            retrospecto = ClubeRetrospecto.builder()
                    .clube(clube)
                    .totalVitorias(0)
                    .totalEmpates(0)
                    .totalDerrotas(totalDerrotas)
                    .totalGolsMarcados(0)
                    .totalGolsSofridos(0)
                    .build();
        } else {
            // Atualizar retrospecto existente
            retrospecto.setTotalDerrotas(totalDerrotas);
        }
        
        // III. Salvar no banco de dados
        ClubeRetrospecto retrospectoSalvo = clubeRetrospectoRepository.save(retrospecto);
        
        // IV. Retornar DTO com dados atualizados
        return ClubeRetrospectoDto.builder()
                .clubeId(retrospectoSalvo.getClube().getId())
                .nomeClube(retrospectoSalvo.getClube().getNome())
                .totalVitorias(retrospectoSalvo.getTotalVitorias())
                .totalEmpates(retrospectoSalvo.getTotalEmpates())
                .totalDerrotas(retrospectoSalvo.getTotalDerrotas())
                .totalGolsMarcados(retrospectoSalvo.getTotalGolsMarcados())
                .totalGolsSofridos(retrospectoSalvo.getTotalGolsSofridos())
                .build();
    }

    public ClubeRetrospectoDto atualizarTotalGolsMarcados(Integer clubeId, Integer totalGolsMarcados) {
        // I. Validar se clube existe
        Clube clube = clubeService.findById(clubeId);

        // II. Buscar retrospecto existente ou criar um novo
        ClubeRetrospecto retrospecto = clubeRetrospectoRepository.findByClubeId(clubeId);

        if (retrospecto == null) {
            retrospecto = ClubeRetrospecto.builder()
                    .clube(clube)
                    .totalVitorias(0)
                    .totalDerrotas(0)
                    .totalEmpates(0)
                    .totalGolsMarcados(totalGolsMarcados)
                    .totalGolsSofridos(0)
                    .build();
        } else {
            // Atualizar retrospecto
            retrospecto.setTotalGolsMarcados(totalGolsMarcados);
        }

        ClubeRetrospecto retrospectoSalvo = clubeRetrospectoRepository.save(retrospecto);

        return ClubeRetrospectoDto.builder()
                .clubeId(retrospectoSalvo.getClube().getId())
                .nomeClube(retrospectoSalvo.getClube().getNome())
                .totalVitorias(retrospectoSalvo.getTotalVitorias())
                .totalEmpates(retrospectoSalvo.getTotalEmpates())
                .totalDerrotas(retrospectoSalvo.getTotalDerrotas())
                .totalGolsMarcados(retrospectoSalvo.getTotalGolsMarcados())
                .totalGolsSofridos(retrospectoSalvo.getTotalGolsSofridos())
                .build();
    }

    public ClubeRetrospectoDto atualizarTotalGolsSofridos(Integer clubeId, Integer totalGolsSofridos) {
        // I. Validar se clube existe
        Clube clube = clubeService.findById(clubeId);

        // II. Buscar retrospecto existente ou criar um novo
        ClubeRetrospecto retrospecto = clubeRetrospectoRepository.findByClubeId(clubeId);

        if (retrospecto == null) {
            retrospecto = ClubeRetrospecto.builder()
                    .clube(clube)
                    .totalVitorias(0)
                    .totalEmpates(0)
                    .totalDerrotas(0)
                    .totalGolsMarcados(0)
                    .totalGolsSofridos(totalGolsSofridos)
                    .build();
        } else {
            retrospecto.setTotalGolsSofridos(totalGolsSofridos);
        }

        ClubeRetrospecto retrospectoSalvo = clubeRetrospectoRepository.save(retrospecto);

        return ClubeRetrospectoDto.builder()
                .clubeId(retrospectoSalvo.getClube().getId())
                .nomeClube(retrospectoSalvo.getClube().getNome())
                .totalVitorias(retrospectoSalvo.getTotalVitorias())
                .totalEmpates(retrospectoSalvo.getTotalEmpates())
                .totalDerrotas(retrospectoSalvo.getTotalDerrotas())
                .totalGolsMarcados(retrospectoSalvo.getTotalGolsMarcados())
                .totalGolsSofridos(retrospectoSalvo.getTotalGolsSofridos())
                .build();
    }

    public ClubeRetrospectoDto buscarRetrospectoPorClube(Integer clubeId) {
        // I. Validar se clube existe
        Clube clube = clubeService.findById(clubeId);
        
        // II. Buscar retrospecto do clube
        ClubeRetrospecto retrospecto = clubeRetrospectoRepository.findByClubeId(clubeId);
        
        // III. Se não existe retrospecto, calcular automaticamente baseado nas partidas
        if (retrospecto == null) {
            return calcularRetrospectoAutomatico(clube);
        }
        
        // IV. Retornar DTO com dados do retrospecto existente
        return ClubeRetrospectoDto.builder()
                .clubeId(retrospecto.getClube().getId())
                .nomeClube(retrospecto.getClube().getNome())
                .totalVitorias(retrospecto.getTotalVitorias())
                .totalEmpates(retrospecto.getTotalEmpates())
                .totalDerrotas(retrospecto.getTotalDerrotas())
                .totalGolsMarcados(retrospecto.getTotalGolsMarcados())
                .totalGolsSofridos(retrospecto.getTotalGolsSofridos())
                .build();
    }

    private ClubeRetrospectoDto calcularRetrospectoAutomatico(Clube clube) {
        // Buscar todas as partidas do clube (como mandante e visitante)
        List<Partida> partidasMandante = partidaRepository.findByClubeMandanteId(clube.getId());
        List<Partida> partidasVisitante = partidaRepository.findByClubeVisitanteId(clube.getId());
        
        int totalVitorias = 0;
        int totalEmpates = 0;
        int totalDerrotas = 0;
        int totalGolsMarcados = 0;
        int totalGolsSofridos = 0;
        
        // Calcular estatísticas como mandante
        for (Partida partida : partidasMandante) {
            String resultado = partida.getResultado();
            if (resultado != null && resultado.contains("x")) {
                String[] gols = resultado.split("x");
                if (gols.length == 2) {
                    try {
                        int golsMandante = Integer.parseInt(gols[0].trim());
                        int golsVisitante = Integer.parseInt(gols[1].trim());
                        
                        totalGolsMarcados += golsMandante;
                        totalGolsSofridos += golsVisitante;
                        
                        if (golsMandante > golsVisitante) {
                            totalVitorias++;
                        } else if (golsMandante == golsVisitante) {
                            totalEmpates++;
                        } else {
                            totalDerrotas++;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar resultados com formato inválido
                    }
                }
            }
        }
        
        // Calcular estatísticas como visitante
        for (Partida partida : partidasVisitante) {
            String resultado = partida.getResultado();
            if (resultado != null && resultado.contains("x")) {
                String[] gols = resultado.split("x");
                if (gols.length == 2) {
                    try {
                        int golsMandante = Integer.parseInt(gols[0].trim());
                        int golsVisitante = Integer.parseInt(gols[1].trim());
                        
                        totalGolsMarcados += golsVisitante;
                        totalGolsSofridos += golsMandante;
                        
                        if (golsVisitante > golsMandante) {
                            totalVitorias++;
                        } else if (golsVisitante == golsMandante) {
                            totalEmpates++;
                        } else {
                            totalDerrotas++;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar resultados com formato inválido
                    }
                }
            }
        }
        
        // Retornar retrospecto calculado automaticamente
        return ClubeRetrospectoDto.builder()
                .clubeId(clube.getId())
                .nomeClube(clube.getNome())
                .totalVitorias(totalVitorias)
                .totalEmpates(totalEmpates)
                .totalDerrotas(totalDerrotas)
                .totalGolsMarcados(totalGolsMarcados)
                .totalGolsSofridos(totalGolsSofridos)
                .build();
    }

    public List<ClubeRetrospectoDto> listarRetrospectoClubes() {
        List<ClubeRetrospecto> retrospectos = clubeRetrospectoRepository.findAll();
        
        return retrospectos.stream()
                .map(retrospecto -> ClubeRetrospectoDto.builder()
                        .clubeId(retrospecto.getClube().getId())
                        .nomeClube(retrospecto.getClube().getNome())
                        .totalVitorias(retrospecto.getTotalVitorias())
                        .totalEmpates(retrospecto.getTotalEmpates())
                        .totalDerrotas(retrospecto.getTotalDerrotas())
                        .totalGolsMarcados(retrospecto.getTotalGolsMarcados())
                        .totalGolsSofridos(retrospecto.getTotalGolsSofridos())
                        .build())
                .collect(Collectors.toList());
    }
}
