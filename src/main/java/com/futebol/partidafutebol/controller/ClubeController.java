package com.futebol.partidafutebol.controller;

import com.futebol.partidafutebol.business.ClubeService;
import com.futebol.partidafutebol.dto.ClubeDto;
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
    public ResponseEntity<List<Clube>> listarTodosClubes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) Boolean ativo) {
        List<Clube> clubes = clubeService.listarClubesComFiltros(nome, uf, ativo);
        return ResponseEntity.ok(clubes);
    }

    @PostMapping
    public ResponseEntity<Clube> salvarClube(@RequestBody Clube clube) {
        Clube clubeSalvo = clubeService.salvarClube(clube);
        return ResponseEntity.ok(clubeSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscarClubePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clubeService.buscarClubePorId(id));
    }

    @DeleteMapping("/{id}/inativar")
    public ResponseEntity<Clube> deletarClubePorId(@PathVariable Integer id) {
        Clube clubeInativado = clubeService.inativarClubePorId(id);
        return ResponseEntity.ok(clubeInativado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarClubePorId(@PathVariable Integer id, @RequestBody ClubeDto clubeDto) {
        clubeService.atualizarClubePorId(id, clubeDto);
        return ResponseEntity.ok().build();
    }

    /*@PutMapping("/{id}/inativar")
    public ResponseEntity<Clube> inativarClubePorId(@PathVariable Integer id) {
        Clube clubeInativado = clubeService.inativarClubePorId(id);
        return ResponseEntity.ok(clubeInativado);
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Clube> ativarClubePorId(@PathVariable Integer id) {
        Clube clubeAtivado = clubeService.ativarClubePorId(id);
        return  ResponseEntity.ok(clubeAtivado);
    }

     */

}
