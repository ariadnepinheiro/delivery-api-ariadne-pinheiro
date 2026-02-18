package com.deliverytech.delivery.controller;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.requests.ProdutoDTO;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Long restauranteIdValido;

    @BeforeEach
    void setUp() {
    
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante para Teste de Produtos");
        restaurante.setCategoria("Variada");
        restaurante.setTaxaEntrega(BigDecimal.valueOf(10.0));
        restaurante.setAtivo(true);
        restaurante = restauranteRepository.save(restaurante);
        restauranteIdValido = restaurante.getId();
    }

    private ProdutoDTO criarProdutoDTO() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("X-Burger Especial");
        dto.setDescricao("Pão, carne de 180g e muito queijo.");
        dto.setCategoria("Lanches");
        dto.setPreco(new BigDecimal("35.90"));
        return dto;
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso - 201")
    void deveCadastrarProduto() throws Exception {
        ProdutoDTO dto = criarProdutoDTO();

        mockMvc.perform(post("/produtos/restaurante/{restauranteId}", restauranteIdValido)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
            
                .andExpect(jsonPath("$.dados.nome", is("X-Burger Especial")))
                .andExpect(jsonPath("$.dados.preco", is(35.90)));
    }

    @Test
    @DisplayName("Deve retornar 404 ao cadastrar produto em restaurante inexistente")
    void deveRetornar404RestauranteInexistente() throws Exception {
        ProdutoDTO dto = criarProdutoDTO();

        mockMvc.perform(post("/produtos/restaurante/{restauranteId}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 400 ao enviar preço negativo")
    void deveRetornar400PrecoInvalido() throws Exception {
        ProdutoDTO dto = criarProdutoDTO();
        dto.setPreco(new BigDecimal("-5.00")); 

        mockMvc.perform(post("/produtos/restaurante/{restauranteId}", restauranteIdValido)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve alternar disponibilidade (Patch) - 200")
    void deveAlternarDisponibilidade() throws Exception {
        String response = mockMvc.perform(post("/produtos/restaurante/{restauranteId}", restauranteIdValido)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarProdutoDTO())))
                .andDo(print()) 
                .andReturn().getResponse().getContentAsString();
        

        Object id = com.jayway.jsonpath.JsonPath.read(response, "$.dados.id");

        mockMvc.perform(patch("/produtos/{id}/disponibilidade", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados.disponivel").exists());
    }
}