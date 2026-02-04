package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.requests.RestauranteDTO;
import com.deliverytech.delivery.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery.service.RestauranteService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> cadastrarRestaurante(@RequestBody @Valid RestauranteDTO dadosDTO) {
        RestauranteResponseDTO restauranteCadastrado = restauranteService.cadastrarRestaurante(dadosDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteCadastrado);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RestauranteResponseDTO>> listarRestaurantes() {
        return ResponseEntity.ok(restauranteService.listarAtivos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarRestaurantePorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarRestaurantePorCategoria(@RequestParam String categoria){
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<RestauranteResponseDTO> toggleEntity(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.toggleAtivo(id));
    }
    
}
