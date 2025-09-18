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
    public ResponseEntity<Estadio> salvarEstadio(@RequestBody Estadio estadio) {
        Estadio estadioSalvo = estadioService.salvarEstadio(estadio);
        return ResponseEntity.ok(estadioSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadio> buscarEstadioPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(estadioService.buscarEstadioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarEstadioPorId(@PathVariable Integer id, @RequestBody EstadioDto estadio) {
        estadioService.atualizarEstadioPorId(id, estadio);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filtros")
    public ResponseEntity<List<Estadio>> listarEstadiosComFiltros(
            @RequestParam(required = false) String nome){
        List<Estadio> estadios = estadioService.listarEstadiosComFiltros(nome);
        return ResponseEntity.ok(estadios);
    }
}
