package com.deliverytech.delivery.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<Restaurante> cadastrarRestaurante(@RequestBody Restaurante restaurante) {
        return ResponseEntity.status(201).body(restauranteService.cadastrarRestaurante(restaurante));
        
    }

    @GetMapping
    public List<Restaurante> listarRestaurantes(@RequestParam(required = false) String nome) {
        return restauranteService.listarAtivos();
    }
    
    @GetMapping("/{id}")
    public Restaurante buscarRestaurantePorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void desativarRestaurante(@PathVariable Long id) {
        restauranteService.desativarRestaurante(id);
    }
    
}
