package com.futebol.partidafutebol.infrastructure.repository;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartidaRepository extends JpaRepository<Partida, Integer> {

    List<Partida> findByClubeMandanteId(Integer clubeMandanteId);

    List<Partida> findByClubeVisitanteId(Integer clubeVisitanteId);

    List<Partida> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Partida> findByEstadioPartidaIdAndDataHoraBetween(Integer estadioId, LocalDateTime inicio, LocalDateTime fim);

}
