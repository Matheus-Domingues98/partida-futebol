package com.futebol.partidafutebol.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estadio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nome", unique = true)
    private String nome;



}
