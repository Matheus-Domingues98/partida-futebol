package com.futebol.partidafutebol.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubeRetrospectoDto {

    private Integer clubeId; // ID do clube para aproveitar
    private String nomeClube; // Nome do clube para facilitar
    private Integer totalVitorias;
    private Integer totalEmpates;
    private Integer totalDerrotas;
    private Integer totalGolsMarcados;
    private Integer totalGolsSofridos;


}
