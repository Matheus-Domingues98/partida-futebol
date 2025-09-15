package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.repository.EstadioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadioService {
    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio salvarEstadio(Estadio estadio) {
        return estadioRepository.saveAndFlush(estadio);
    }

    public List<Estadio> listarTodosEstadios() {
        return estadioRepository.findAll();
    }

    public void deletarEstadioPorNome(String nome) {
        estadioRepository.deleteByNome(nome);
    }

    public Estadio buscarEstadioPorNome(String nome) {
        return estadioRepository.findByNome(nome).orElseThrow(
                () -> new RuntimeException("Estadio nao encontrado")
        );
    }

    public void atualizarEstadioPorId(Integer id, Estadio estadio) {
        Estadio estadioEntity = estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estadio nao encontrado"));
        Estadio estadioAtualizado = Estadio.builder()
                .nome(estadio.getNome() != null ? estadio.getNome() : estadioEntity.getNome())
                .id(estadioEntity.getId())
                .build();

        estadioRepository.saveAndFlush(estadioAtualizado);
    }
}
