package com.deliverytech.delivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarClienteComSucesso() throws Exception{
        String email = "novo." + System.currentTimeMillis() + "@email.com";

        String json = """
                    {
                        "nome": "Ariadne Pinheiro",
                        "email": "%s",
                        "telefone":"1199999-0000",
                        "endereco":"Rua A, 5"
                    }
                """.formatted(email);

                mockMvc.perform(post("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andDo(resultado -> {
                        System.out.println(resultado.getResponse().getStatus());
                        System.out.println(resultado.getResponse().getContentAsString());
                    })
                    .andExpect(status().isCreated())
                    /*  .andExpect(jsonPath("$.dados.id").exists())
                    .andExpect(jsonPath("$.dados.nome").value("Ariadne Pinheiro"))
                    .andExpect(jsonPath("$.dados.email").value(email)) */
                    ;
    }

    @Test
    void deveRetornarDadosErroQuandoDadosInvalidos() throws  Exception{
        String json = """
                    {
                        "nome": "",
                        "email": "@",
                        "telefone":"",
                        "endereco":"Rua"
                    }
                """;
        mockMvc.perform(post("/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andDo(resultado -> {
                        System.out.println(resultado.getResponse().getStatus());
                        System.out.println(resultado.getResponse().getContentAsString());
                })
            .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarClientesAtivos() throws Exception{
        mockMvc.perform(get("/clientes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        String email = "novo." + System.currentTimeMillis() + "@email.com";

        String json = """
                    {
                        "nome": "Ariadne Pinheiro",
                        "email": "%s",
                        "telefone":"1199999-0000",
                        "endereco":"Rua A, 5"
                    }
                """.formatted(email);

                MvcResult result = mockMvc.perform(post("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andDo(resultado -> {
                        System.out.println(resultado.getResponse().getStatus());
                        System.out.println(resultado.getResponse().getContentAsString());
                    })
                    .andExpect(status().isCreated())
                    .andReturn();

                String clienteCriado = result.getResponse().getContentAsString();

                Integer idInt = JsonPath.read(clienteCriado, "$.dados.id");
                Long id = idInt.longValue();

                mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados.nome").value("Ariadne Pinheiro"))
                .andExpect(jsonPath("$.dados.email").value(email));
    }

    @Test
    void deveRetornarNotFoundQuandoClienteNaoExistir() throws Exception{
            mockMvc.perform(get("/clientes/{id}", 9999L))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveAtivarEDesativarCliente() throws Exception{
        String email = "novo." + System.currentTimeMillis() + "@email.com";

        String json = """
                    {
                        "nome": "Ariadne Pinheiro",
                        "email": "%s",
                        "telefone":"1199999-0000",
                        "endereco":"Rua A, 5"
                    }
                """.formatted(email);

                MvcResult result = mockMvc.perform(post("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andReturn();

                String clienteCriado = result.getResponse().getContentAsString();

                Integer idInt = JsonPath.read(clienteCriado, "$.dados.id");
                Long id = idInt.longValue();

                mockMvc.perform(patch("/clientes/{id}/toggle", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados.ativo").value(false));
    }
}