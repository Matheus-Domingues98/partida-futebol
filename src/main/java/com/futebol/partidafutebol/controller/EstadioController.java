package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.EstadioService;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estadios")

public class EstadioController {

    private final EstadioService estadioService;

    @PostMapping
    public ResponseEntity<Estadio> salvarEstadio(@RequestBody Estadio estadio) {
        Estadio estadioSalvo = estadioService.salvarEstadio(estadio);
        return ResponseEntity.ok(estadioSalvo);
    }

    @GetMapping
    public ResponseEntity<Estadio> buscarEstadioPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(estadioService.buscarEstadioPorNome(nome));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarEstadioPoNome(@RequestParam String nome) {
        estadioService.deletarEstadioPorNome(nome);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarEstadioPorId(@RequestParam Integer id, @RequestBody Estadio estadio) {
        estadioService.atualizarEstadioPorId(id, estadio);
        return ResponseEntity.ok().build();
    }
}
