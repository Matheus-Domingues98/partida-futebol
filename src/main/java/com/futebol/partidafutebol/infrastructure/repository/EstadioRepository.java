package com.futebol.partidafutebol.infrastructure.repository;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Integer> {

    Optional<Estadio> findByNome(String nome);

    List<Estadio> findByNomeContainingIgnoreCase(String nome);


    @Transactional
    void deleteByNome(String nome);
}
