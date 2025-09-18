package com.futebol.partidafutebol.dto;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import java.time.LocalDateTime;

public class PartidaDto {

    private Clube clubeMandante;
    private Clube clubeVisitante;
    private Estadio estadioPartida;
    private LocalDateTime dataHora;
    private String resultado;

    public PartidaDto() {
    }

    public PartidaDto(Clube clubeMandante, Clube clubeVisitante, LocalDateTime dataHora, Estadio estadioPartida, String resultado) {
        this.clubeMandante = clubeMandante;
        this.clubeVisitante = clubeVisitante;
        this.dataHora = dataHora;
        this.estadioPartida = estadioPartida;
        this.resultado = resultado;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(Clube clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Estadio getEstadioPartida() {
        return estadioPartida;
    }

    public void setEstadioPartida(Estadio estadioPartida) {
        this.estadioPartida = estadioPartida;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
