package com.deliverytech.delivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.deliverytech.delivery.dto.requests.ClienteDTO;
import com.deliverytech.delivery.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
/* import com.deliverytech.delivery.config.ModelMapperConfig; */
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

    public List<ClienteResponseDTO> listarAtivos() {

        return clienteRepository.findByAtivoTrue()
        .stream()
        .map(clientes -> modelMapper.map(clientes, ClienteResponseDTO.class))
        .toList();

    }

    public List<ClienteResponseDTO> buscarPorNome(String nome){

        return clienteRepository.findByNomeContainingIgnoreCase(nome)
        .stream()
        .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
        .toList();

    }

    public ClienteResponseDTO buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            return modelMapper.map(cliente, ClienteResponseDTO.class);

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

        Cliente clienteExistente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        clienteExistente.setAtivo(!clienteExistente.getAtivo());
        return modelMapper.map(clienteExistente, ClienteResponseDTO.class);

    }
}
