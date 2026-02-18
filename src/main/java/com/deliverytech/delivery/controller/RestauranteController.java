package com.deliverytech.delivery.controller;

import java.util.URI;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.dto.requests.RestauranteDTO;
import com.deliverytech.delivery.dto.responses.PagedResponse;
import com.deliverytech.delivery.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery.service.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(
    value = "/restaurantes", 
    produces = "application/json")
@CrossOrigin(origins = "*")
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes.")
public class RestauranteController {
    
    @Autowired
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @Operation(summary = "Cadastrar um novo restaurante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
        @ApiResponse(responseCode = "409", description = "Conflito: Nome de restaurante já existe.")
    })

    @PostMapping
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<RestauranteResponseDTO>> cadastrarRestaurante(@RequestBody @Valid RestauranteDTO dadosDTO) {
        RestauranteResponseDTO restauranteCadastrado = restauranteService.cadastrarRestaurante(dadosDTO);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(restauranteCadastrado.getId())
            .toUri();
        
        return ResponseEntity.created(location)
            .header("Content-Type", "application/json")
            .body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(restauranteCadastrado));
    }

    @Operation(summary = "Listar restaurantes ativos (paginado).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes ativos retornada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos.")
    })

    @GetMapping("/listar")
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> listarRestaurantes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = restauranteService.listarAtivos(pageable);
        var response = new PagedResponse<>(pageResult);

        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
            .body(response);
    }

    @Operation(summary = "Buscar restaurante por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<RestauranteResponseDTO>> buscarRestaurantePorId(@PathVariable Long id) {
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(restauranteService.buscarPorId(id)));
    }

    @Operation(summary = "Buscar restaurantes por Id (paginado).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurantes encontrados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado para essa categoria.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<RestauranteResponseDTO>> buscarRestaurantePorIdAtivo(@PathVariable Long id) {
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(restauranteService.buscarPorIdAtivo(id)));
    }

    @Operation(summary = "Buscar restaurantes por categoria (paginado).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurantes encontrados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado para essa categoria.")
    })
    @GetMapping("/categoria")
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> buscarRestaurantePorCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
        ){
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = restauranteService.buscarPorCategoria(categoria, pageable);
        var response = new PagedResponse<>(pageResult);

        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
            .body(response);
    }

    @Operation(summary = "Ativar/Desativar restaurante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status do restaurante atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<RestauranteResponseDTO>>toggle(@PathVariable Long id) {
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(restauranteService.toggleAtivo(id)));
    }
}
