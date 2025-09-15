package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.ClubeService;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @GetMapping
    public ResponseEntity<List<Clube>> listarTodosClubes() {
        List<Clube> clubes = clubeService.listarTodosClubes();
        return ResponseEntity.ok(clubes);
    }

    @PostMapping
    public ResponseEntity<Clube> salvarClube(@RequestBody Clube clube) {
        Clube clubeSalvo = clubeService.salvarClube(clube);
        return ResponseEntity.ok(clubeSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscarClubePorNome(@PathVariable String nome) {
        return ResponseEntity.ok(clubeService.buscarClubePorNome(nome));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarClubePoNome(@PathVariable String nome) {
        clubeService.deletarClubePorNome(nome);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarClubePorId(@PathVariable Integer id, @RequestBody Clube clube) {
        clubeService.atualizarClubePorId(id, clube);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/inativar")
    public ResponseEntity<Clube> inativarClubePorId(@PathVariable Integer id) {
        Clube clubeInativado = clubeService.inativarClubePorId(id);
        return ResponseEntity.ok(clubeInativado);
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Clube> ativarClubePorId(@PathVariable Integer id) {
        Clube clubeAtivado = clubeService.ativarClubePorId(id);
        return  ResponseEntity.ok(clubeAtivado);
    }
}
