package com.futebol.partidafutebol.dto;

import java.time.LocalDateTime;

import static com.futebol.partidafutebol.infrastructure.entitys.Clube.dtf;

public class ClubeDto {

    private String nome;
    private String uf;
    private LocalDateTime dataCriacao;
    private boolean ativo;

    public ClubeDto() {
    }

    public ClubeDto(String nome, String uf, LocalDateTime dataCriacao, boolean ativo) {
        this.nome = nome;
        this.uf = uf;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "ClubeDto{" +
                "ativo=" + ativo +
                ", nome='" + nome + '\'' +
                ", uf='" + uf + '\'' +
                ", dataCriacao=" + dtf.format(dataCriacao) +
                '}';
    }
}
