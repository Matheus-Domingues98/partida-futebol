package com.futebol.partidafutebol.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubeRankingDto {
    private Integer clubeId;
    private String nomeClube;
    private int totalPontos;
    private int totalVitorias;
    private int totalEmpates;
    private int totalDerrotas;
    private int totalGolsMarcados;
    private int totalGolsSofridos;
    private int totalJogos;
}
