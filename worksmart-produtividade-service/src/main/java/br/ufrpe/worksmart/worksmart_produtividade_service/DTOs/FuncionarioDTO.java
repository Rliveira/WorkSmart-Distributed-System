package br.ufrpe.worksmart.worksmart_produtividade_service.DTOs;

import br.ufrpe.worksmart.worksmart_usuario_service.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO para representar o Funcionário recebido do worksmart-usuario-service
// DEVE espelhar os campos da entidade Funcionario lá, incluindo os de Usuario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {
    // Campos de Usuario herdados
    private Long id;
    private String nome;
    private String cpf;
    private String login;

    // Campos específicos de Funcionario
    private Double taxaProdutividade;
    private Cargo cargo;
    private LocalDate dataContratacao;
    private Double salario;
    private String horarioJornada;
    private Boolean eAdm;
    private String filial;
    private String dispositivoChave;
}