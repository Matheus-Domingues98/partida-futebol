package com.futebol.partidafutebol.infrastructure.entitys;

import com.futebol.partidafutebol.infrastructure.entitys.aux.UF;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

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
    @Enumerated(EnumType.STRING)
    private UF uf;
    
    @Column(name = "dataCriacao")
    private LocalDate dataCriacao;

    @Column(name = "status")
    private boolean ativo;
}
