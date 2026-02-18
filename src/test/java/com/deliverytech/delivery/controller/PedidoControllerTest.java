package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.requests.ItemPedidoDTO;
import com.deliverytech.delivery.dto.requests.PedidoDTO;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PedidoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    
    @Autowired private RestauranteRepository restauranteRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ClienteRepository clienteRepository;

    private Long clienteId;
    private Long restauranteId;
    private Long produtoId;

    @BeforeEach
    void setUp() {
        
        Cliente cliente = new Cliente();
        cliente.setNome("Ariadne");
        cliente.setAtivo(true);
        cliente.setEmail("ariadne@email.com");
        cliente = clienteRepository.save(cliente);
        clienteId = cliente.getId();

        
        Restaurante rest = new Restaurante();
        rest.setNome("Burger King");
        rest.setTaxaEntrega(BigDecimal.valueOf(5.0));
        rest = restauranteRepository.save(rest);
        restauranteId = rest.getId();

        
        Produto prod = new Produto();
        prod.setNome("Whopper");
        prod.setPreco(BigDecimal.valueOf(30.0));
        prod.setRestaurante(rest);
        prod = produtoRepository.save(prod);
        produtoId = prod.getId();
    }

    private PedidoDTO criarPedidoDTO() {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(clienteId);
        dto.setRestauranteId(restauranteId);
        dto.setEnderecoEntrega("Rua dos Testes, 123");
        
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(produtoId);
        item.setQuantidade(2);
        dto.setItens(List.of(item));
        
        return dto;
    }

    @Test
    @DisplayName("Criação bem-sucedida - Status 201")
    void deveCriarPedidoComSucesso() throws Exception {
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarPedidoDTO())))
                .andExpect(status().isCreated()) // Ou .isOk() dependendo do seu Controller
                .andExpect(jsonPath("$.dados.status", is("PENDENTE")))
                .andExpect(jsonPath("$.dados.nomeCliente", is("João Silva")));
    }

    @Test
    @DisplayName("Conflito de dados - Status 409 (Avanço inválido)")
    void deveRetornar409AoAvancarPedidoPendente() throws Exception {
        
        String response = mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarPedidoDTO())))
                .andReturn().getResponse().getContentAsString();

        Integer id = com.jayway.jsonpath.JsonPath.read(response, "$.dados.id");

        
        mockMvc.perform(patch("/pedidos/{id}/status/avancar", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Status é inválido")));
    }

    @Test
    @DisplayName("Busca inexistente - Status 404")
    void deveRetornar404ClienteInexistente() throws Exception {
        mockMvc.perform(get("/pedidos/cliente/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Paginação - Metadados corretos")
    void deveValidarMetadadosPaginacao() throws Exception {
        mockMvc.perform(get("/pedidos/cliente/{id}", clienteId)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.totalElements").exists());
    }
}