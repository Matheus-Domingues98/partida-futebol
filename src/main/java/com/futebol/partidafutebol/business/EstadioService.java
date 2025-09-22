package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.EstadioDto;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.repository.EstadioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadioService {

    private final EstadioRepository estadioRepository;
    // 1. Cadastrar estadio
    @Transactional
    public EstadioDto cadastrarEstadio(EstadioDto estadioDto) {

        // I. Validar se o estadio ja existe
        if (estadioRepository.existsByNome(estadioDto.getNome())) {
            throw new IllegalArgumentException("Estadio ja existe");
        }

        // II. Converter DTO da Entity (usando Builder da Entity)
        Estadio estadio = Estadio.builder().nome(estadioDto.getNome()).build();

        // III. Salvar no banco de dados
        Estadio estadioSalvo = estadioRepository.save(estadio);

        // IV. Retornar DTO com dados salvos
        return new EstadioDto(estadioSalvo.getNome());
    }
    // 2. Editar estadio
    @Transactional
    public EstadioDto editarEstadio(EstadioDto estadioDto, Integer id) {

        // I. Validar se o estadio existe
        Estadio estadioExistente = findById(id);

        // II. Converter DTO da Entity (usando Builder da Entity)
        Estadio estadioAtualizado = Estadio.builder()
                .id(estadioExistente.getId())
                .nome(estadioDto.getNome() != null ? estadioDto.getNome() : estadioExistente.getNome())
                .build();

        // III. Salvar no banco de dados
        Estadio estadioSalvo = estadioRepository.save(estadioAtualizado);

        // IV. Retornar DTO com dados salvos
        return new EstadioDto(estadioSalvo.getNome());
    }
    // 3. Buscar um estadio
    public EstadioDto buscarEstadioPorId(Integer id) {
        // I. Validar se o estadio existe
        Estadio estadio = findById(id);
        // II. Retornar DTO com dados salvos
        return new EstadioDto(estadio.getNome());
    }
    // 4. Listar todos os estadios
    public List<EstadioDto> listarTodosEstadios(String nome) {

        // Se nenhum filtro foi fornecido, retorna todos
        if (nome == null) {
            return estadioRepository.findAll().stream()
                    .map(estadio -> new EstadioDto(estadio.getNome()))
                    .collect(Collectors.toList());
        } else {
            return estadioRepository.findByNomeContainingIgnoreCase(nome).stream()
                    .map(estadio -> new EstadioDto(estadio.getNome()))
                    .collect(Collectors.toList());
        }
    }
    // Metodo generico
    public Estadio findById(Integer id) {
        Estadio estadioEntity = estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estadio nao encontrado"));
        return estadioEntity;
    }
}
