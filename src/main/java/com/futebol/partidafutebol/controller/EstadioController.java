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

    // 1. Cadastar estadio
    @PostMapping
    public ResponseEntity<EstadioDto> salvarEstadio(@RequestBody EstadioDto estadioDto) {
        EstadioDto estadioSalvo = estadioService.cadastrarEstadio(estadioDto);
        return ResponseEntity.ok(estadioSalvo);
    }
    // 2. Editar estadio
    @PutMapping("/{id}")
    public ResponseEntity<EstadioDto> editarEstadio(@RequestBody EstadioDto estadioDto, @PathVariable Integer id) {
        EstadioDto estadioEditado = estadioService.editarEstadio(estadioDto, id);
        return ResponseEntity.ok(estadioEditado);
    }
    // 3. Buscar estadio por id
    @GetMapping("/{id}")
    public ResponseEntity<EstadioDto> buscarEstadioPorId(@PathVariable Integer id) {
        EstadioDto estadioDto = estadioService.buscarEstadioPorId(id);
        return ResponseEntity.ok(estadioDto);
    }
    // 4. Listar Estadio
    @GetMapping
    public ResponseEntity<List<EstadioDto>> listarTodosEstadios(
            @RequestParam(required = false) String nome){
        List<EstadioDto> estadiosListados = estadioService.listarTodosEstadios(nome);
        return ResponseEntity.ok(estadiosListados);
    }
}
