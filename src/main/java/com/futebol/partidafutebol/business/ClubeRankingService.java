package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.ClubeRankingDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClubeRankingService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    public List<ClubeRankingDto> gerarRanking(String criterio) {
        List<Clube> clubes = clubeRepository.findAll();
        List<Partida> partidas = partidaRepository.findAll();

        Map<Integer, ClubeRankingDto> mapa = new HashMap<>();

        for (Clube clube : clubes) {
            mapa.put(clube.getId(), ClubeRankingDto.builder()
                    .clubeId(clube.getId())
                    .nomeClube(clube.getNome())
                    .totalPontos(0)
                    .totalVitorias(0)
                    .totalEmpates(0)
                    .totalDerrotas(0)
                    .totalGolsMarcados(0)
                    .totalGolsSofridos(0)
                    .totalJogos(0)
                    .build()
            );
        }

        for (Partida partida : partidas) {
            if (partida.getGolsMandante() == null || partida.getGolsVisitante() == null) continue;

            ClubeRankingDto mandante = mapa.get(partida.getClubeMandante().getId());
            ClubeRankingDto visitante = mapa.get(partida.getClubeVisitante().getId());

            int golsMandante = partida.getGolsMandante();
            int golsVisitante = partida.getGolsVisitante();

            // Atualiza jogos
            mandante.setTotalJogos(mandante.getTotalJogos() + 1);
            visitante.setTotalJogos(visitante.getTotalJogos() + 1);

            // Atualiza gols
            mandante.setTotalGolsMarcados(mandante.getTotalGolsMarcados() + golsMandante);
            mandante.setTotalGolsSofridos(mandante.getTotalGolsSofridos() + golsVisitante);

            visitante.setTotalGolsMarcados(visitante.getTotalGolsMarcados() + golsVisitante);
            visitante.setTotalGolsSofridos(visitante.getTotalGolsSofridos() + golsMandante);

            // Atualiza resultado
            if (golsMandante > golsVisitante) {
                mandante.setTotalVitorias(mandante.getTotalVitorias() + 1);
                visitante.setTotalDerrotas(visitante.getTotalDerrotas() + 1);
            } else if (golsMandante < golsVisitante) {
                visitante.setTotalVitorias(visitante.getTotalVitorias() + 1);
                mandante.setTotalDerrotas(mandante.getTotalDerrotas() + 1);
            } else {
                mandante.setTotalEmpates(mandante.getTotalEmpates() + 1);
                visitante.setTotalEmpates(visitante.getTotalEmpates() + 1);
            }
        }

        // Calcular pontos
        for (ClubeRankingDto dto : mapa.values()) {
            int pontos = dto.getTotalVitorias() * 3 + dto.getTotalEmpates();
            dto.setTotalPontos(pontos);
        }

        // Convert to list
        List<ClubeRankingDto> ranking = new ArrayList<>(mapa.values());

        // Aplicar filtro e ordenação baseado no critério
        switch (criterio.toLowerCase()) {
            case "pontos":
                return ranking.stream()
                        .filter(c -> c.getTotalPontos() > 0)
                        .sorted(Comparator.comparingInt(ClubeRankingDto::getTotalPontos).reversed())
                        .toList();

            case "gols":
                return ranking.stream()
                        .filter(c -> c.getTotalGolsMarcados() > 0)
                        .sorted(Comparator.comparingInt(ClubeRankingDto::getTotalGolsMarcados).reversed())
                        .toList();

            case "vitorias":
                return ranking.stream()
                        .filter(c -> c.getTotalVitorias() > 0)
                        .sorted(Comparator.comparingInt(ClubeRankingDto::getTotalVitorias).reversed())
                        .toList();

            case "jogos":
                return ranking.stream()
                        .filter(c -> c.getTotalJogos() > 0)
                        .sorted(Comparator.comparingInt(ClubeRankingDto::getTotalJogos).reversed())
                        .toList();

            default:
                throw new IllegalArgumentException("Critério inválido: " + criterio);
        }
    }


}
