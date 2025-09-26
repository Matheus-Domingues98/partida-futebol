package com.futebol.partidafutebol.infrastructure.repository;

import com.futebol.partidafutebol.infrastructure.entitys.ClubeRetrospecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ClubeRetrospectoRepository extends JpaRepository<ClubeRetrospecto, Integer> {

    // Buscar retrospecto por ID do clube (relacionamento)
    ClubeRetrospecto findByClubeId(Integer clubeId);
    
    // Verificar se existe retrospecto para um clube
    boolean existsByClubeId(Integer clubeId);
}
