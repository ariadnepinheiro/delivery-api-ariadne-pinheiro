package com.deliverytech.delivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
/* import com.deliverytech.delivery_api.config.ModelMapperConfig; */
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    public ClienteService(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    /*public Cliente cadastrarCliente(Cliente cliente) {
        if(clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }*/

    public ClienteResponseDTO cadastrarCliente(ClienteDTO clienteDTO) {
        if(clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    public List<Cliente> buscarPorNome(String nome){
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    /*public Cliente atualizarCliente(Long id, Cliente dadosAtualizados) {
        Cliente clienteExistente = buscarPorId(id);

        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setEmail(dadosAtualizados.getEmail());
        clienteExistente.setTelefone(dadosAtualizados.getTelefone());
        clienteExistente.setEndereco(dadosAtualizados.getEndereco());

        return clienteRepository.save(clienteExistente);
    }

    public void desativarCliente(Long id) {
        Cliente clienteExistente = buscarPorId(id);
        clienteExistente.setAtivo(false);
        clienteRepository.save(clienteExistente);
    }*/


    @Transactional
    public ClienteResponseDTO toggleAtivoCliente(Long id) {
        Cliente clienteExistente = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: "));
        clienteExistente.setAtivo(!clienteExistente.isAtivo());
        return modelMapper.map(clienteExistente, ClienteResponseDTO.class);
    }
}
