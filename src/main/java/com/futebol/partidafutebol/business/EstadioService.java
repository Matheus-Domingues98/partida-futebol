package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.EstadioDto;
import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.repository.EstadioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadioService {
    private final EstadioRepository estadioRepository;

    public Estadio salvarEstadio(Estadio estadio) {
        return estadioRepository.saveAndFlush(estadio);
    }

    public List<Estadio> listarTodosEstadios() {
        return estadioRepository.findAll();
    }

    public Estadio buscarEstadioPorId(Integer id) {
        return estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estadio nao encontrado")
        );
    }

    @Transactional
    public void atualizarEstadioPorId(Integer id, EstadioDto estadioDto) {
        Estadio estadioEntity = estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estadio nao encontrado"));
        Estadio estadioAtualizado = Estadio.builder()
                .nome(estadioDto.getNome() != null ? estadioDto.getNome() : estadioEntity.getNome())
                .id(estadioEntity.getId())
                .build();

        estadioRepository.saveAndFlush(estadioAtualizado);
    }

    public List<Estadio> listarEstadiosComFiltros(String nome) {
        // Se nenhum filtro foi fornecido, retorna todos
        if (nome == null) {
            return estadioRepository.findAll();
        } else {
            return estadioRepository.findByNomeContainingIgnoreCase(nome);
        }
    }
}
