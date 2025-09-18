package com.futebol.partidafutebol.infrastructure.repository;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import com.futebol.partidafutebol.infrastructure.entitys.aux.UF;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
@Transactional
public interface ClubeRepository extends JpaRepository<Clube, Integer> {

    Optional<Clube> findByNome(String nome);

    @Transactional
    void deleteByNome(String nome);

    List<Clube> findByAtivoTrue();
    List<Clube> findByAtivoFalse();
    List<Clube> findByUf(UF uf);
    List<Clube> findByNomeContainingIgnoreCase(String nome);

    Optional<Clube> findByNomeAndAtivoTrue(String nome);

    boolean existsByNome(String nome);
    
    // Métodos combinados para filtros mais eficientes
    List<Clube> findByUfAndAtivo(UF uf, Boolean ativo);
    List<Clube> findByNomeContainingIgnoreCaseAndAtivo(String nome, Boolean ativo);
    List<Clube> findByUfAndNomeContainingIgnoreCase(UF uf, String nome);
    List<Clube> findByUfAndNomeContainingIgnoreCaseAndAtivo(UF uf, String nome, Boolean ativo);

}
