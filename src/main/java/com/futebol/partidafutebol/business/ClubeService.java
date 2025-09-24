package com.futebol.partidafutebol.business;
import com.futebol.partidafutebol.dto.ClubeDto;
import com.futebol.partidafutebol.dto.PartidaDto;
import com.futebol.partidafutebol.exception.DadosInvalidosExcepcion;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Partida;
import com.futebol.partidafutebol.infrastructure.entitys.aux.UF;
import com.futebol.partidafutebol.infrastructure.repository.ClubeRepository;
import com.futebol.partidafutebol.infrastructure.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final PartidaService partidaService;
    private final PartidaRepository partidaRepository;

    // 1. Cadastrar clube:
    @Transactional
    public ClubeDto cadastrarClube(ClubeDto clubeDto) {
        // I. Validar dados do clube
        validarClube(clubeDto);

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
        return new ClubeDto(
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
        Clube clubeEntity = findById(id);
        // II. Validar dados do clube
        validarClube(clubeDto);

        // III. Atualizar dados do clube
        Clube clubeAtualizado = Clube.builder()
                .id(clubeEntity.getId())
                .nome(clubeDto.getNome() != null ? clubeDto.getNome() : clubeEntity.getNome())
                .uf(clubeDto.getUf() != null ? clubeDto.getUf() : clubeEntity.getUf())
                .dataCriacao(clubeDto.getDataCriacao() != null ? clubeDto.getDataCriacao() : clubeEntity.getDataCriacao())
                .ativo(clubeDto.isAtivo())
                .build();

        //IV. Salvar no banco
        clubeRepository.saveAndFlush(clubeAtualizado);

        // V. Retornar DTO com dados atualizados
        return new ClubeDto(
                clubeAtualizado.getNome(),
                clubeAtualizado.getUf(),
                clubeAtualizado.getDataCriacao(),
                clubeAtualizado.isAtivo());
    }

    // 3. Inativar clube:
    @Transactional
    public ClubeDto inativarClubePorId(Integer id) {
        // I. Validar se clube existe
        Clube clubeEntity = findById(id);
        // II. Inativar clube
        clubeEntity.setAtivo(false);

        // III. Salvar no banco
        clubeRepository.saveAndFlush(clubeEntity);

        // IV. Retornar clube inativado
        return new ClubeDto(
                clubeEntity.getNome(),
                clubeEntity.getUf(),
                clubeEntity.getDataCriacao(),
                clubeEntity.isAtivo());
    }

    // 4. Buscar clube por ID:
    public ClubeDto buscarClubePorId(Integer id) {
        // I. Validar se clube existe
        Clube clubeEntity = findById(id);

        // II. Retornar DTO com dados do clube
        return new ClubeDto(
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

    // Metodo generico
    public Clube findById(Integer id) {
        return clubeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
    }

    public ClubeDto validarClube(ClubeDto clubeDto) {
        // validar dados minimos mencionados
        if (clubeDto.getNome().trim().isEmpty() || clubeDto.getUf() == null || clubeDto.getDataCriacao() == null || clubeDto.isAtivo() == false ||
                clubeDto.getNome().trim().length() < 2 || clubeDto.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os dados devem ser preenchidos");
        }
        // Validar se clube já existe
        if (clubeRepository.existsByNome(clubeDto.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube já existe");
        }
        // Validar data de criação
        validarDataCriacao(clubeDto);

        // Conflito de dados

        return clubeDto;
    }

    private void validarDataCriacao(ClubeDto clubeDto) {
        // Validar se data de criação não é no futuro
        if (clubeDto.getDataCriacao() != null && clubeDto.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A data de criação do clube não pode ser no futuro");
        }
    }
}



