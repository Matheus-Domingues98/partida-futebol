package com.futebol.partidafutebol.dto;

public class EstadioDto {

    private String nome;

    public EstadioDto() {
    }

    public EstadioDto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
