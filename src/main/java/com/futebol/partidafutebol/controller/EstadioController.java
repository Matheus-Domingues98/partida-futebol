package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.EstadioService;
import com.futebol.partidafutebol.dto.EstadioDto;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estadios")

public class EstadioController {

    private final EstadioService estadioService;

    @GetMapping
    public ResponseEntity<List<Estadio>> listarTodosEstadios() {
        List<Estadio> estadios = estadioService.listarTodosEstadios();
        return ResponseEntity.ok(estadios);
    }

    @PostMapping
    public ResponseEntity<Estadio> salvarEstadio(@RequestBody EstadioDto estadio) {
        Estadio estadioSalvo = estadioService.salvarEstadio(estadio);
        return ResponseEntity.ok(estadioSalvo);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<Estadio> buscarEstadioPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(estadioService.buscarEstadioPorNome(nome));
    }

    @DeleteMapping("/{nome}")
    public ResponseEntity<Void> deletarEstadioPorNome(@PathVariable String nome) {
        estadioService.deletarEstadioPorNome(nome);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarEstadioPorId(@PathVariable Integer id, @RequestBody EstadioDto estadio) {
        estadioService.atualizarEstadioPorId(id, estadio);
        return ResponseEntity.ok().build();
    }
}
