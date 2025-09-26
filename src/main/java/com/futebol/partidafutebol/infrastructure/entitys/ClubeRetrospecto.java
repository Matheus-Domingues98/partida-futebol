package com.futebol.partidafutebol.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clube_retrospecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubeRetrospecto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "clube_id")
    private Clube clube;

    @Column(name = "total_vitorias")
    private Integer totalVitorias;

    @Column(name = "total_empates")
    private Integer totalEmpates;

    @Column(name = "total_derrotas")
    private Integer totalDerrotas;

    @Column(name = "total_gols_marcados")
    private Integer totalGolsMarcados;

    @Column(name = "total_gols_sofridos")
    private Integer totalGolsSofridos;

}
