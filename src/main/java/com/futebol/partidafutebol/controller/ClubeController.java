package com.futebol.partidafutebol.controller;
import com.futebol.partidafutebol.business.ClubeService;
import com.futebol.partidafutebol.business.ClubeRetrospectoService;
import com.futebol.partidafutebol.business.ClubeRankingService;
import com.futebol.partidafutebol.dto.ClubeDto;
import com.futebol.partidafutebol.dto.ClubeRankingDto;
import com.futebol.partidafutebol.dto.ClubeRetrospectoDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;
    private final ClubeRetrospectoService clubeRetrospectoService;
    private final ClubeRankingService clubeRankingService;

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

    // 6. Buscar retrospecto de um clube específico: OK
    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<ClubeRetrospectoDto> buscarRetrospectoPorClube(@PathVariable Integer id) {
        ClubeRetrospectoDto retrospecto = clubeRetrospectoService.buscarRetrospectoPorClube(id);
        return ResponseEntity.ok(retrospecto);
    }

    // 7. Listar retrospecto de todos os clubes: OK
    @GetMapping("/retrospecto")
    public ResponseEntity<List<ClubeRetrospectoDto>> listarRetrospectoClubes() {
        List<ClubeRetrospectoDto> retrospectos = clubeRetrospectoService.listarRetrospectoClubes();
        return ResponseEntity.ok(retrospectos);
    }
    // 8. Buscar retrospecto de um clube específico contra OS adversários: OK
    @GetMapping("/{id}/retrospecto/{adversarioId}")
    public ResponseEntity<?> RetrospectoContraAdversario(
            @PathVariable Integer id,
            @PathVariable Integer adversarioId) {
        try {
            List<ClubeRetrospectoDto> retrospectos = clubeRetrospectoService.retrospectoContraAdversario(id, adversarioId);
            return ResponseEntity.ok(retrospectos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Clube ou adversário não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao calcular o retrospecto");
        }
    }

    // 9. Confronto direto entre dois clubes: OK
    @GetMapping("/{id}/confronto/{adversarioId}")
    public ResponseEntity<?> ConfrontoDireto(
            @PathVariable Integer id,
            @PathVariable Integer adversarioId) {
        try {
            List<ClubeRetrospectoDto> retrospectos = clubeRetrospectoService.confrontoDireto(id, adversarioId);
            return ResponseEntity.ok(retrospectos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Clube ou adversário não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao calcular o retrospecto");
        }
    }

    // 10. Ranking de clubes:
    @GetMapping("/ranking")
    public ResponseEntity<List<ClubeRankingDto>> listarRankingClubes(@RequestParam(defaultValue = "pontos") String criterio) {
        List<ClubeRankingDto> ranking = clubeRankingService.gerarRanking(criterio);
        return ResponseEntity.ok(ranking);
    }
}
