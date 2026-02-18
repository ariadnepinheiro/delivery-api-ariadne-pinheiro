package com.deliverytech.delivery.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
/*import jakarta.validation.constraints.Pattern;*/
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para criação e atualização de cliente")
public class ClienteDTO {

    @Schema(description = "Nome do cliente", example = "João da Silva")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    @Schema(description = "Email do cliente", example = "joao.silva@exemplo.com")
    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;

    @Schema(description = "Telefone do cliente", example = "(11) 98765-4321")
    @NotBlank(message = "O telefone é obrigatório")
    /*@Pattern(regexp="\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}", message="Telefone inválido. Formato esperado: (XX) XXXXX-XXXX ou XX XXXX-XXXX ou similar")*/
    private String telefone;
    
    @Schema(description = "Endereço do cliente", example = "Rua Exemplo, 123, Bairro, Cidade, Estado")
    @Size(min = 8, max = 100, message = "O endereço deve ter entre 8 e 100 caracteres")
    private String endereco;
    
}
