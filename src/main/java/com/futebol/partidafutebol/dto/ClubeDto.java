package com.futebol.partidafutebol.dto;

import java.util.Date;

public class ClubeDto {

    private String nome;
    private String uf;
    private Date dataCriacao;
    private boolean ativo;

    public ClubeDto() {
    }

    public ClubeDto(String nome, String uf, Date dataCriacao, boolean ativo) {
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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
