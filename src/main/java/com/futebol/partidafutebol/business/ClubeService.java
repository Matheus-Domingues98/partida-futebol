package com.futebol.partidafutebol.business;
import com.futebol.partidafutebol.dto.ClubeDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.aux.UF;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;

    // 1. Cadastrar clube:
    @Transactional
    public ClubeDto cadastrarClube(ClubeDto clubeDto) {

        // I. Validar se clube já existe
        // Correto:
        Optional<Clube> clubeExistente = clubeRepository.findByNome(clubeDto.getNome());
        if (clubeExistente.isPresent()) {
            throw new RuntimeException("Clube já existe: " + clubeDto.getNome());
        }

        // II. Converter DTO para Entity (usando Builder da Entity)
        Clube clube = Clube.builder()
                .nome(clubeDto.getNome())
                .uf(clubeDto.getUf())
                .dataCriacao(clubeDto.getDataCriacao())
                .ativo(clubeDto.isAtivo())
                .build();

        // III. Salvar no banco
        Clube clubeSalvo = clubeRepository.save(clube);

        // IV. Retornar DTO com dados salvos
        return new ClubeDto (
                clubeSalvo.getNome(),
                clubeSalvo.getUf(),
                clubeSalvo.getDataCriacao(),
                clubeSalvo.isAtivo()
        );
    }
    // 2. Editar clube:
    @Transactional
    public ClubeDto editarClube(ClubeDto clubeDto, Integer id) {

        // I. Validar se clube existe
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));

        // II. Atualizar dados do clube
        Clube clubeAtualizado = Clube.builder()
                .id(clubeEntity.getId())
                .nome(clubeDto.getNome() != null ? clubeDto.getNome() : clubeEntity.getNome())
                .uf(clubeDto.getUf() != null ? clubeDto.getUf() : clubeEntity.getUf())
                .dataCriacao(clubeDto.getDataCriacao() != null ? clubeDto.getDataCriacao() : clubeEntity.getDataCriacao())
                .ativo(clubeDto.isAtivo())
                .build();

        //III. Salvar no banco
        clubeRepository.saveAndFlush(clubeAtualizado);

        // IV. Retornar DTO com dados atualizados
        return new ClubeDto (
                clubeAtualizado.getNome(),
                clubeAtualizado.getUf(),
                clubeAtualizado.getDataCriacao(),
                clubeAtualizado.isAtivo());
    }
    // 3. Inativar clube:
    @Transactional
    public Clube inativarClubePorId(Integer id) {
        // I. Validar se clube existe
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));

        // II. Inativar clube
        clubeEntity.setAtivo(false);

        // III. Salvar no banco
        clubeRepository.saveAndFlush(clubeEntity);

        // IV. Retornar clube inativado
        return new ClubeDto (
                clubeEntity.getNome(),
                clubeEntity.getUf(),
                clubeEntity.getDataCriacao(),
                clubeEntity.isAtivo());
    }
    // 4. Buscar clube por ID:
    public ClubeDto buscarClubePorId(Integer id) {
        Clube clubeEntity = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube nao encontrado"));

        return new ClubeDto (
                clubeEntity.getNome(),
                clubeEntity.getUf(),
                clubeEntity.getDataCriacao(),
                clubeEntity.isAtivo()
        );
    }
    // 5. Listar todos os clubes com filtros otimizados:
    public List<ClubeDto> listarTodosClubes(String nome, String uf, LocalDate dataCriacao, Boolean ativo) {
        List<Clube> clubes;
        
        // Converter UF string para enum se fornecido
        UF ufEnum = null;
        if (uf != null && !uf.trim().isEmpty()) {
            try {
                ufEnum = UF.valueOf(uf.toUpperCase());
            } catch (IllegalArgumentException e) {
                // UF inválida, retorna lista vazia
                return List.of();
            }
        }
        
        // Estratégia: Usar métodos combinados do Repository para máxima eficiência
        if (nome != null && ufEnum != null && ativo != null) {
            // Filtro completo: nome + UF + ativo
            clubes = clubeRepository.findByUfAndNomeContainingIgnoreCaseAndAtivo(ufEnum, nome, ativo);
        } else if (ufEnum != null && nome != null) {
            // Filtro: UF + nome
            clubes = clubeRepository.findByUfAndNomeContainingIgnoreCase(ufEnum, nome);
        } else if (nome != null && ativo != null) {
            // Filtro: nome + ativo
            clubes = clubeRepository.findByNomeContainingIgnoreCaseAndAtivo(nome, ativo);
        } else if (ufEnum != null && ativo != null) {
            // Filtro: UF + ativo
            clubes = clubeRepository.findByUfAndAtivo(ufEnum, ativo);
        } else if (nome != null) {
            // Filtro: apenas nome
            clubes = clubeRepository.findByNomeContainingIgnoreCase(nome);
        } else if (ufEnum != null) {
            // Filtro: apenas UF
            clubes = clubeRepository.findByUf(ufEnum);
        } else if (ativo != null) {
            // Filtro: apenas ativo
            clubes = ativo ? clubeRepository.findByAtivoTrue() : clubeRepository.findByAtivoFalse();
        } else {
            // Sem filtros: todos os clubes
            clubes = clubeRepository.findAll();
        }
        
        // Aplicar filtro de data em memória (não há método no Repository para isso)
        return clubes.stream()
                .filter(clube -> dataCriacao == null || clube.getDataCriacao().equals(dataCriacao))
                .map(clube -> new ClubeDto(
                        clube.getNome(),
                        clube.getUf(),
                        clube.getDataCriacao(),
                        clube.isAtivo()
                ))
                .collect(Collectors.toList());
    }
}




