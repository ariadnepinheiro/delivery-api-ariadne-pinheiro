package com.deliverytech.delivery.controller;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
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

import com.deliverytech.delivery.dto.requests.ClienteDTO;
import com.deliverytech.delivery.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery.dto.responses.PagedResponse;
import com.deliverytech.delivery.service.ClienteService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/clientes",
        produces = "application/json")  

@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes.")
public class ClienteController {

    @Autowired
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Cadastrar novo cliente.")
    @ApiResponses(
                value={
                    @ApiResponse(responseCode="201", description="Cliente cadastrado com sucesso."),
                    @ApiResponse(responseCode="400", description="Erro de validação."),
                }
    )

    @PostMapping
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<ClienteResponseDTO>> cadastrarCliente(@Valid @RequestBody ClienteDTO cliente) {
        ClienteResponseDTO clienteCriado = clienteService.cadastrarCliente(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteCriado.getId())
                .toUri();

        return ResponseEntity.created(location).header("Content-Type", "application/json").body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(clienteCriado));
    }

    @Operation(summary = "Listar clientes ativos (paginado).")
    @ApiResponses(
                value={
                    @ApiResponse(responseCode="200", description="Lista de clientes ativos retornada com sucesso."),
                    @ApiResponse(responseCode="404", description="Cliente não encontrado."),
                }
    )

    @GetMapping
    public ResponseEntity<PagedResponse<ClienteResponseDTO>> listarClientes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = clienteService.listarClientesAtivos(pageable);
        var response = new PagedResponse<ClienteResponseDTO>(pageResult)
        return ResponseEntity.ok()
        .header("Content-Type", "application/json")
        .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
        .body(response);
    }
    
    @Operation(summary = "Buscar cliente por ID.")
    @ApiResponses(
                value={
                    @ApiResponse(responseCode="200", description="Cliente encontrado com sucesso."),
                    @ApiResponse(responseCode="404", description="Cliente não encontrado."),
                }
    )

    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<ClienteResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().header("Content-Type", "application/json").body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(clienteService.buscarPorId(id)));
    }

    /*@PutMapping("/{id}")
    public ClienteResponseDTO atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO novoCliente) {
        return clienteService.atualizarCliente(id, novoCliente);
    }*/

    @Operation(summary = "Alternar status ativo/inativo do cliente.")
    @ApiResponses(
                value={
                    @ApiResponse(responseCode="200", description="Status do cliente alternado com sucesso."),
                    @ApiResponse(responseCode="404", description="Cliente não encontrado."),
                }
    )
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<com.deliverytech.delivery.dto.responses.ApiResponse<ClienteResponseDTO>> toggleAtivo(@PathVariable Long id){
        return ResponseEntity.ok().header("Content-Type", "application/json").body(new com.deliverytech.delivery.dto.responses.ApiResponse<>(clienteService.toggleAtivoCliente(id)));
    }

}
