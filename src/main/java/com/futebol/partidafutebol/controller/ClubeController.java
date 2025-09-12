package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.ClubeService;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @PostMapping
    public ResponseEntity<Clube> salvarClube(@RequestBody Clube clube) {
        Clube clubeSalvo = clubeService.salvarClube(clube);
        return ResponseEntity.ok(clubeSalvo);
    }

    @GetMapping
    public ResponseEntity<Clube> buscarClubePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(clubeService.buscarClubePorNome(nome));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarClubePoNome(@RequestParam String nome) {
        clubeService.deletarClubePorNome(nome);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarClubePorId(@RequestParam Integer id, @RequestBody Clube clube) {
        clubeService.atualizarClubePorId(id, clube);
        return ResponseEntity.ok().build();
    }
}
