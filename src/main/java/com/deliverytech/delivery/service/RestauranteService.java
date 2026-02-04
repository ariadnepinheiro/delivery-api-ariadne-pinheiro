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
        restaurante.setAvaliacao(BigDecimal.ZERO);
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return mapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

     public List<RestauranteResponseDTO> listarAtivos(){
        return restauranteRepository.findByAtivoTrue()
        .stream()
        .map(restaurante -> mapper.map(restaurante, RestauranteResponseDTO.class))
        .toList();
    }

    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria){
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria)
            .stream()
            .map(restaurante -> mapper.map(restaurante, RestauranteResponseDTO.class))
            .toList();
    }

    public RestauranteResponseDTO buscarPorId(Long id){
        Restaurante restaurante = restauranteRepository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Restaurante não encontrado."));
        return mapper.map(restaurante, RestauranteResponseDTO.class);
    }

    /*public void desativarRestaurante(Long id){
        RestauranteResponseDTO restaurante =  buscarPorId(id);
        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    }*/

    @Transactional
    public RestauranteResponseDTO toggleAtivo(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        restaurante.setAtivo(!restaurante.getAtivo());

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return mapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }
}
