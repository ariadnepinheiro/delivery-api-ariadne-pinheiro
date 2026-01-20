package com.deliverytech.delivery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Service
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;

    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante cadastrarRestaurante(Restaurante dadosRestaurante) {
        dadosRestaurante.setAtivo(true);
        return restauranteRepository.save(dadosRestaurante);
    }

     public List<Restaurante> listarAtivos(){
        return restauranteRepository.findByAtivoTrue();
    }

    public List<Restaurante> buscarPorCategoria(String categoria){
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria);
    }

    public Restaurante buscarPorId(Long id){
        return restauranteRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("Restaurante não encontrado."));
    }

    public void desativarRestaurante(Long id){
        Restaurante restaurante =  buscarPorId(id);
        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    } 
}
