package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.requests.RestauranteDTO;
import com.deliverytech.delivery.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;

import jakarta.transaction.Transactional;

@Service
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;
    private final ModelMapper mapper;    

    public RestauranteService(RestauranteRepository restauranteRepository, ModelMapper mapper) {
        this.restauranteRepository = restauranteRepository;
        this.mapper = mapper;
    }

    @Transactional
    public RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO restauranteDTO) {
        if(restauranteRepository.existsByNome(restauranteDTO.getNome())) {
            throw new BusinessException("Restaurante de mesmo nome já foi cadastrado.");
        }
        Restaurante restaurante = mapper.map(restauranteDTO, Restaurante.class);
        restaurante.setAtivo(true);
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return mapper.map(restauranteSalvo, RestauranteResponseDTO.class);
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

    public Object toggleAtivo(Long id) {
        Restaurante restaurante = buscarPorId(id);
        restaurante.setAtivo(!restaurante.getAtivo());
        restauranteRepository.save(restaurante);
        return null;
    }
}
