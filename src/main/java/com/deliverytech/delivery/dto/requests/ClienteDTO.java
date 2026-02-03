package com.deliverytech.delivery.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\\\(?\\\\d{2}\\\\)?[\\\\s-]?\\\\d{4,5}-?\\\\d{4}", message = "Telefone inválido. Formato esperado: (XX) XXXXX-XXXX ou XX XXXX-XXXX ou similar")
    private String telefone;

    @Size(min = 8, max = 100, message = "O endereço deve ter entre 8 e 100 caracteres")
    private String endereco;
    
}
