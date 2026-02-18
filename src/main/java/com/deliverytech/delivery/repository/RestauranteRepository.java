package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    Page<Restaurante> findByAtivoTrue(Pageable pageable);
    List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
    Page<Restaurante> findByCategoriaAndAtivoTrue(String categoria, Pageable pageable);
    Boolean existsByNome(String nome);
    
}
