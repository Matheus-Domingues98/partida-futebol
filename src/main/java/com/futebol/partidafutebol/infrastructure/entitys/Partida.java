package com.futebol.partidafutebol.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "partida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "clube_mandante_id")
    private Clube clubeMandante;

    @ManyToOne
    @JoinColumn(name = "clube_visitante_id")
    private Clube clubeVisitante;

    @ManyToOne
    @JoinColumn(name = "estadio_partida_id")
    private Estadio estadioPartida;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "golsMandante")
    private Integer golsMandante;

    @Column(name = "golsVisitante")
    private Integer golsVisitante;


}
