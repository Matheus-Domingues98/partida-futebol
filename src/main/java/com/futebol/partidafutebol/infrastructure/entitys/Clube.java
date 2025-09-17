package com.futebol.partidafutebol.infrastructure.entitys;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "clube")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clube {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "nome", unique = true)
    private String nome;
    
    @Column(name = "UF")
    private String uf;
    
    @Column(name = "dataCriacao")
    private LocalDateTime dataCriacao;

    @Column(name = "status")
    private boolean ativo;

    @Override
    public String toString() {
        return "Clube{" +
                "ativo=" + ativo +
                ", id=" + id +
                ", nome='" + nome + '\'' +
                ", uf='" + uf + '\'' +
                ", dataCriacao=" + (dataCriacao != null ? dtf.format(dataCriacao) : "null") +
                '}';
    }
}
