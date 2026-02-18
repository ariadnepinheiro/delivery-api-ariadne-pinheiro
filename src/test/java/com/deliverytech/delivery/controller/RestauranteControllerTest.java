package com.deliverytech.delivery.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.requests.RestauranteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RestauranteDTO criarDTOValido() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria do Bairro");
        dto.setCategoria("Pizzaria");
        dto.setEndereco("Rua das Flores, 123");
        dto.setTelefone("(11) 98765-4321"); 
        dto.setTaxaEntrega(new BigDecimal("10.50"));
        return dto;
    }

    @Test
    @DisplayName("Criação bem-sucedida - Status 201")
    void deveCadastrarRestaurante() throws Exception {
        RestauranteDTO dto = criarDTOValido();

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dados.nome").value("Pizzaria do Bairro"));
    }

    @Test
    @DisplayName("Dados inválidos - Status 400")
    void deveRetornar400QuandoTelefoneInvalido() throws Exception {
        RestauranteDTO dto = criarDTOValido();
        dto.setTelefone("123"); 

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Busca inexistente - Status 404")
    void deveRetornar404BuscaInexistente() throws Exception {
        mockMvc.perform(get("/restaurantes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Paginação - Metadados corretos")
    void deveValidarPaginacaoEMetadados() throws Exception {

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOValido())));

        mockMvc.perform(get("/restaurantes/listar")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Conflito de dados - Status 409")
    void deveRetornar409SeHouverDuplicidade() throws Exception {
        RestauranteDTO dto = criarDTOValido();

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }
}