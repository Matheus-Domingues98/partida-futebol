package com.futebol.partidafutebol.controller;
import com.futebol.partidafutebol.business.ClubeService;
import com.futebol.partidafutebol.dto.ClubeDto;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    // 1. Cadastrar um clube:
    @PostMapping
    public ResponseEntity<ClubeDto> salvarClube(@RequestBody ClubeDto clubeDto) {
        ClubeDto clubeSalvo = clubeService.cadastrarClube(clubeDto);
        return ResponseEntity.ok(clubeSalvo);
    }
    // 2. Editar um clube:
    @PutMapping("/{id}")
    public ResponseEntity<ClubeDto> editarClube(@RequestBody ClubeDto clubeDto, @PathVariable Integer id) {
        ClubeDto clubeEditado = clubeService.editarClube(clubeDto, id);
        return ResponseEntity.ok(clubeEditado);
    }
    // 3. Inativar um clube:
    @DeleteMapping("/{id}/inativar")
    public ResponseEntity<ClubeDto> inativarClubePorId(@PathVariable Integer id) {
        ClubeDto clubeInativado = clubeService.inativarClubePorId(id);
        return ResponseEntity.ok(clubeInativado);
    }
    // 4. Buscar clube por Id:
    @GetMapping("/{id}")
    public ResponseEntity<ClubeDto> buscarClubePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clubeService.buscarClubePorId(id));
    }
    // 5. Listar todos os clubes:
    @GetMapping
    public ResponseEntity<List<ClubeDto>> listarTodosClubes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) LocalDate dataCriacao,
            @RequestParam(required = false) Boolean ativo) {
        List<ClubeDto> clubesListados = clubeService.listarTodosClubes(nome, uf, dataCriacao, ativo);
        return ResponseEntity.ok(clubesListados);
    }
}
