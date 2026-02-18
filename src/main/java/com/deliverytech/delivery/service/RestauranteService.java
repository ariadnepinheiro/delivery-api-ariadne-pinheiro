package com.deliverytech.delivery.service;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.requests.RestauranteDTO;
import com.deliverytech.delivery.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;

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

    public Page<RestauranteResponseDTO> listarAtivos(Pageable pageable){
        return restauranteRepository.findByAtivoTrue(pageable)
            .map(restaurante -> mapper.map(restaurante, RestauranteResponseDTO.class));
    }

    public Page<RestauranteResponseDTO> buscarPorCategoria(String categoria, Pageable pageable){
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria, Pageable.ofSize(10))
            .map(restaurante -> mapper.map(restaurante, RestauranteResponseDTO.class));
    }

    public RestauranteResponseDTO buscarPorId(Long id){
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(()-> new EntityNotFoundException("Restaurante não encontrado."));
        return mapper.map(restaurante, RestauranteResponseDTO.class);
    }

    @Transactional
    public RestauranteResponseDTO toggleAtivo(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        restaurante.setAtivo(!restaurante.getAtivo());

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return mapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }
}
