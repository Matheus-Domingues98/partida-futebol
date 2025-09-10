package com.futebol.partidafutebol.infrastructure.repository;

import com.futebol.partidafutebol.infrastructure.entitys.Clube;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@Transactional
public interface ClubeRepository extends JpaRepository<Clube, Integer> {

    Optional<Clube> findByNome(String nome);


    @Transactional
    void deleteByNome(String nome);
}
