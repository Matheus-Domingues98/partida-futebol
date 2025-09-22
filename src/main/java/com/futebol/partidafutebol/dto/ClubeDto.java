package com.futebol.partidafutebol.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.futebol.partidafutebol.infrastructure.entitys.aux.UF;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public class ClubeDto {

    private String nome;
    private UF uf;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataCriacao;
    private boolean ativo;

    public ClubeDto(String nome, UF uf, LocalDate dataCriacao, boolean ativo) {
        this.nome = nome;
        this.uf = uf;
        this.dataCriacao = dataCriacao; 
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UF getUf() {
        return uf;
    }

    public void setUf(UF uf) {
        this.uf = uf;
    }
}
