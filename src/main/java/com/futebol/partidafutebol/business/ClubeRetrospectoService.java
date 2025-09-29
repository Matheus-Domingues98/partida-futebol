package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.ClubeDto;
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

import java.util.*;
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

    //==================================================================================
    // Buscar retrospecto de um clube específico
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

    // Metodo que pertence a buscarRetrospectoPorClube
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
            if (partida.getGolsMandante() != null && partida.getGolsVisitante() != null) {
                int golsMandante = partida.getGolsMandante();
                int golsVisitante = partida.getGolsVisitante();

                totalGolsMarcados += golsMandante;
                totalGolsSofridos += golsVisitante;

                if (golsMandante > golsVisitante) {
                    totalVitorias++;
                } else if (golsMandante == golsVisitante) {
                    totalEmpates++;
                } else {
                    totalDerrotas++;
                }
            }
        }

        // Calcular estatísticas como visitante
        for (Partida partida : partidasVisitante) {
            if (partida.getGolsMandante() != null && partida.getGolsVisitante() != null) {
                int golsMandante = partida.getGolsMandante();
                int golsVisitante = partida.getGolsVisitante();

                totalGolsMarcados += golsVisitante;
                totalGolsSofridos += golsMandante;

                if (golsVisitante > golsMandante) {
                    totalVitorias++;
                } else if (golsVisitante == golsMandante) {
                    totalEmpates++;
                } else {
                    totalDerrotas++;
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
        // Buscar todos os clubes ativos (sem filtros, apenas ativos)
        List<ClubeDto> clubesDto = clubeService.listarTodosClubes(null, null, null, true);
        
        // Calcular retrospecto automaticamente para cada clube
        return clubesDto.stream()
                .map(clubeDto -> {
                    // Buscar entidade Clube pelo ID
                    Clube clube = clubeService.findById(clubeDto.getId());
                    return calcularRetrospectoAutomatico(clube);
                })
                .collect(Collectors.toList());
    }

    public List<ClubeRetrospectoDto> retrospectoContraAdversario(Integer clubeId, Integer adversarioId) {
        // I. Validar se clube e adversário existem
        Clube clube = clubeService.findById(clubeId);
        Clube adversario = clubeService.findById(adversarioId);

        // II. Buscar partidas específicas entre os dois clubes
        List<Partida> partidasMandante = partidaRepository.findByClubeMandanteIdAndClubeVisitanteId(clubeId, adversarioId);
        List<Partida> partidasVisitante = partidaRepository.findByClubeMandanteIdAndClubeVisitanteId(adversarioId, clubeId);
        
        List<Partida> todasPartidas = new ArrayList<>();
        todasPartidas.addAll(partidasMandante);
        todasPartidas.addAll(partidasVisitante);

        // III. Inicializar contadores
        int totalVitorias = 0;
        int totalEmpates = 0;
        int totalDerrotas = 0;
        int totalGolsMarcados = 0;
        int totalGolsSofridos = 0;

        // IV. Processar partidas entre os dois clubes específicos
        for (Partida partida : todasPartidas) {
            if (partida.getGolsMandante() != null && partida.getGolsVisitante() != null) {
                boolean ehMandante = partida.getClubeMandante().getId().equals(clubeId);
                
                Integer golsPro = ehMandante ? partida.getGolsMandante() : partida.getGolsVisitante();
                Integer golsContra = ehMandante ? partida.getGolsVisitante() : partida.getGolsMandante();
                
                // V. Atualizar estatísticas
                totalGolsMarcados += golsPro;
                totalGolsSofridos += golsContra;
                
                if (golsPro > golsContra) {
                    totalVitorias++;
                } else if (golsPro.equals(golsContra)) {
                    totalEmpates++;
                } else {
                    totalDerrotas++;
                }
            }
        }

        // VI. Criar DTO com retrospecto específico contra o adversário
        ClubeRetrospectoDto dto = ClubeRetrospectoDto.builder()
                .clubeId(clubeId)
                .nomeClube(clube.getNome())
                .totalVitorias(totalVitorias)
                .totalEmpates(totalEmpates)
                .totalDerrotas(totalDerrotas)
                .totalGolsMarcados(totalGolsMarcados)
                .totalGolsSofridos(totalGolsSofridos)
                .build();

        return Arrays.asList(dto);
    }

    public List<ClubeRetrospectoDto> confrontoDireto(Integer clubeId, Integer adversarioId) {
        // I. Validar se clube e adversário existem
        Clube clube = clubeService.findById(clubeId);
        Clube adversario = clubeService.findById(adversarioId);

        // II. Buscar partidas específicas entre os dois clubes
        List<Partida> partidasMandante = partidaRepository.findByClubeMandanteIdAndClubeVisitanteId(clubeId, adversarioId);
        List<Partida> partidasVisitante = partidaRepository.findByClubeMandanteIdAndClubeVisitanteId(adversarioId, clubeId);

        List<Partida> todasPartidas = new ArrayList<>();
        todasPartidas.addAll(partidasMandante);
        todasPartidas.addAll(partidasVisitante);

        // III. Inicializar contadores
        int totalVitorias = 0;
        int totalEmpates = 0;
        int totalDerrotas = 0;
        int totalGolsMarcados = 0;
        int totalGolsSofridos = 0;

        // IV. Processar partidas considerando se o clube é mandante ou visitante
        for (Partida p : todasPartidas) {
            if (p.getGolsMandante() != null && p.getGolsVisitante() != null) {
                boolean ehMandante = p.getClubeMandante().getId().equals(clubeId);
                
                Integer golsPro = ehMandante ? p.getGolsMandante() : p.getGolsVisitante();
                Integer golsContra = ehMandante ? p.getGolsVisitante() : p.getGolsMandante();
                
                // Atualizar estatísticas do clube principal
                totalGolsMarcados += golsPro;
                totalGolsSofridos += golsContra;
                
                if (golsPro > golsContra) {
                    totalVitorias++;
                } else if (golsPro.equals(golsContra)) {
                    totalEmpates++;
                } else {
                    totalDerrotas++;
                }
            }
        }

        // VI. Criar DTO com retrospecto específico contra o adversário
        ClubeRetrospectoDto dto = ClubeRetrospectoDto.builder()
                .clubeId(clubeId)
                .nomeClube(clube.getNome())
                .totalVitorias(totalVitorias)
                .totalEmpates(totalEmpates)
                .totalDerrotas(totalDerrotas)
                .totalGolsMarcados(totalGolsMarcados)
                .totalGolsSofridos(totalGolsSofridos)
                .build();
        ClubeRetrospectoDto dto2 = ClubeRetrospectoDto.builder()
                .clubeId(adversarioId)
                .nomeClube(adversario.getNome())        // Nome do adversário
                .totalVitorias(totalDerrotas)           // Derrotas do clube = Vitórias do adversário
                .totalEmpates(totalEmpates)             // Empates são iguais para ambos
                .totalDerrotas(totalVitorias)           // Vitórias do clube = Derrotas do adversário
                .totalGolsMarcados(totalGolsSofridos)   // Gols sofridos pelo clube = Gols marcados pelo adversário
                .totalGolsSofridos(totalGolsMarcados)   // Gols marcados pelo clube = Gols sofridos pelo adversário
                .build();

        return Arrays.asList(dto,dto2);
    }
}
