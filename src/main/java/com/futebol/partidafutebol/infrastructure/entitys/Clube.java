package com.futebol.partidafutebol.infrastructure.entitys;

import lombok.*;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "clube")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clube {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "nome", unique = true)
    private String nome;
    
    @Column(name = "UF")
    private String uf;
    
    @Column(name = "dataCriacao")
    private Date dataCriacao;

    @Column(name = "status")
    private boolean ativo;
}
