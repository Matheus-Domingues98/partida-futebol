package com.futebol.partidafutebol.infrastructure.entitys;

import com.futebol.partidafutebol.dto.EstadioDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "estadio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", unique = true)
    private String nome;

    public Estadio(EstadioDto data) {
        this.nome = data.getNome();
    }

}
