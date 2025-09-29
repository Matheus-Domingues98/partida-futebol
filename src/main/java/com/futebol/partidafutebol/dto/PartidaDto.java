package com.futebol.partidafutebol.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class PartidaDto {

    private Integer clubeMandanteId;
    private Integer clubeVisitanteId;
    private Integer estadioPartidaId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;
    private Integer golsMandante;
    private Integer golsVisitante;

    public PartidaDto() {
    }

    public PartidaDto(Integer clubeMandanteId, Integer clubeVisitanteId, LocalDateTime dataHora, Integer estadioPartidaId, Integer golsMandante, Integer golsVisitante) {
        this.clubeMandanteId = clubeMandanteId;
        this.clubeVisitanteId = clubeVisitanteId;
        this.dataHora = dataHora;
        this.estadioPartidaId = estadioPartidaId;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public Integer getClubeMandanteId() {
        return clubeMandanteId;
    }

    public void setClubeMandanteId(Integer clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public Integer getClubeVisitanteId() {
        return clubeVisitanteId;
    }

    public void setClubeVisitanteId(Integer clubeVisitanteId) {
        this.clubeVisitanteId = clubeVisitanteId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getEstadioPartidaId() {
        return estadioPartidaId;
    }

    public void setEstadioPartidaId(Integer estadioPartidaId) {
        this.estadioPartidaId = estadioPartidaId;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }
}