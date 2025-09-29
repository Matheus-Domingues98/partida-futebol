package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.PartidaService;
import com.futebol.partidafutebol.dto.PartidaDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partidas")

public class PartidaController {

    private final PartidaService partidaService;

    // 1. Cadastrar partida
    @PostMapping
    public ResponseEntity<PartidaDto> cadastrarPartida(@RequestBody PartidaDto partidaDto) {
        PartidaDto partidaSalva = partidaService.cadastrarPartida(partidaDto);
        return ResponseEntity.ok(partidaSalva);
    }

    //2 . Editar partida
    @PutMapping("/{id}")
    public ResponseEntity<PartidaDto> editarPartida(@RequestBody PartidaDto partidaDto, @PathVariable Integer id) {
        PartidaDto partidaEditada = partidaService.editarPartida(partidaDto, id);
        return ResponseEntity.ok(partidaEditada);
    }

    // 3. Remover partida
    @DeleteMapping("/{id}")
    public ResponseEntity<PartidaDto> excluirPartida(@PathVariable Integer id) {
        PartidaDto partidaExcluida = partidaService.excluirPartida(id);
        return ResponseEntity.ok(partidaExcluida);
    }

    // 4. Buscar partida por id
    @GetMapping("/{id}")
    public ResponseEntity<PartidaDto> buscarPartidaPorId(@PathVariable Integer id) {
        PartidaDto partidaDto = partidaService.buscarPartidaPorId(id);
        return ResponseEntity.ok(partidaDto);
    }

    // 5. Listar todas as partidas
    @GetMapping
    public ResponseEntity<List<PartidaDto>> listarTodasPartidas(
            @RequestParam(required = false) Integer clubeMandanteId,
            @RequestParam(required = false) Integer clubeVisitanteId,
            @RequestParam(required = false) Integer estadioPartidaId,
            @RequestParam(required = false) LocalDateTime dataHora,
            @RequestParam(required = false) Integer golsMandante,
            @RequestParam(required = false) Integer golsVisitante) {
        List<PartidaDto> partidasListadas = partidaService.listarTodasPartidas(clubeMandanteId, clubeVisitanteId, estadioPartidaId, dataHora, golsMandante, golsVisitante);
        return ResponseEntity.ok(partidasListadas);
    }
}
