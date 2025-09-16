package com.futebol.partidafutebol.dto;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;

import java.util.Date;

public class PartidaDto {

    private Clube clubeMandante;
    private Clube clubeVisitante;
    private Estadio estadioPartida;
    private Date dataHora;
    private String resultado;

    public PartidaDto() {
    }

    public PartidaDto(Clube clubeMandante, Clube clubeVisitante, Date dataHora, Estadio estadioPartida, String resultado) {
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

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
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
