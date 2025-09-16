package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.PartidaService;
import com.futebol.partidafutebol.dto.PartidaDto;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partidas")

public class PartidaController {

    private final PartidaService partidaService;

    @GetMapping
    public ResponseEntity<List<Partida>> listarTodasPartidas() {
        List<Partida> partidas = partidaService.listarTodasPartidas();
        return ResponseEntity.ok(partidas);
    }

    @PostMapping
    public ResponseEntity<Partida> salvarPartida(@RequestBody PartidaDto partida) {
        if (partida.getClubeMandante().equals(partida.getClubeVisitante())) {
            throw  new IllegalArgumentException("Clubes devem ser diferentes");
        }
        return  ResponseEntity.ok(partidaService.salvarPartida(partida));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscarPartidaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(partidaService.buscarPartidaPorId(id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Partida> deletarPartidaPorId(@PathVariable Integer id) {
        partidaService.deletarPartidaPorId(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> atualizarPartidaPorId(@PathVariable Integer id, @RequestBody PartidaDto partida) {
        partidaService.atualizarPartidaPorId(id, partida);
        return ResponseEntity.ok().build();
    }
}
