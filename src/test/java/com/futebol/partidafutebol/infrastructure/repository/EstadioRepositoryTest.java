package com.futebol.partidafutebol.infrastructure.repository;

import com.futebol.partidafutebol.dto.EstadioDto;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.futebol.partidafutebol.infrastructure.entitys")
class EstadioRepositoryTest {
    
    @Autowired
    private EstadioRepository estadioRepository;

    @Test
    @DisplayName("Deve encontrar um estadio pelo nome")
    void findByNomeCase1() {
        String nome = "Maracanã";

        EstadioDto data = new EstadioDto(nome);
        this.createEstadio(data);

        Optional<Estadio> result = this.estadioRepository.findByNome(nome);

        assertThat(result.isPresent()).isTrue();
    }

    private Estadio createEstadio(EstadioDto data) {
        Estadio newEstadio = new Estadio(data);
        return estadioRepository.save(newEstadio);
    }
}