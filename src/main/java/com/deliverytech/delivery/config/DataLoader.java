package com.deliverytech.delivery.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

import java.util.Arrays;
import java.math.BigDecimal;

@Configuration
public class DataLoader {
    /**
     * @param clienteRepository
     * @param restauranteRepository
     * @param produtoRepository
     * @param pedidoRepository
     * @return
     */
    @Bean
    public CommandLineRunner initData(
        ClienteRepository clienteRepository,
        RestauranteRepository restauranteRepository,
        ProdutoRepository produtoRepository,
        PedidoRepository pedidoRepository
    ){
        return args ->
        {
            System.out.println("Iniciando carregamento de dados...");

            Cliente cliente1 = new Cliente();
            cliente1.setNome("Raiel Landre");
            cliente1.setEmail("raiel@gmail.com");
            cliente1.setTelefone("11987654321");
            cliente1.setEndereco("Rua das Flores, 123, São Paulo, SP");
            cliente1.setAtivo(true);

            Cliente cliente2 = new Cliente();
            cliente2.setNome("Giovanni de Carvalho");
            cliente2.setEmail("giovanni@gmail.com");
            cliente2.setTelefone("11912345678");
            cliente2.setEndereco("Avenida Brasil, 456, Rio de Janeiro, RJ");
            cliente2.setAtivo(true);

            /*clienteRepository.save(cliente1);
            clienteRepository.save(cliente2); */

            clienteRepository.saveAll(Arrays.asList(cliente1, cliente2));

            Restaurante r1 = new Restaurante();
            r1.setNome("Pizza");
            r1.setCategoria("Pizzaria");
            r1.setEndereco("Rua das Pizzas, 100, São Paulo, SP");
            r1.setTelefone("11999998888");
            r1.setAvaliacao(new BigDecimal("4.5"));
            r1.setTaxaEntrega(new BigDecimal("5.00"));
            r1.setAtivo(true);

            Restaurante r2 = new Restaurante();
            r1.setNome("Burger House");
            r1.setCategoria("Hamburgueria");
            r1.setEndereco("Rua Augusta, 500 - São Paulo/SP");
            r1.setTelefone("11999998888");
            r1.setAvaliacao(new BigDecimal("4.0"));
            r1.setTaxaEntrega(new BigDecimal("8.00"));
            r1.setAtivo(true);

            restauranteRepository.saveAll(Arrays.asList(r1, r2));

            Produto p1 = new Produto();
            p1.setNome("Pizza de Calabresa");
            p1.setDescricao("Deliciosa pizza de calabresa com borda recheada");
            p1.setPreco(new BigDecimal("35.00"));
            p1.setCategoria("Pizza");
            p1.setDisponivel(true);
            p1.setRestaurante(r1);

            Produto p2 = new Produto();
            p2.setNome("Hambúrguer Clássico");
            p2.setDescricao("Hambúrguer com queijo, alface, tomate e molho especial");
            p2.setPreco(new BigDecimal("25.00"));
            p2.setCategoria("Hambúrguer");
            p2.setDisponivel(true);
            p2.setRestaurante(r2);

            produtoRepository.saveAll(Arrays.asList(p1, p2));
            
            System.out.println("Carregamento de dados concluído.");
        };
    }   
}