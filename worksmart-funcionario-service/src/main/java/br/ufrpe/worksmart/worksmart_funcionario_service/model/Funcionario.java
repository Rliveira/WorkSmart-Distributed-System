package br.ufrpe.worksmart.worksmart_funcionario_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate; // Para dataContratacao

// Para o enum Cargo
enum Cargo {
    DESENVOLVEDOR, GERENTE, ANALISTA, SUPORTE, OUTRO
}


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática de ID
    private Long id;

    // Atributos de Usuário (do diagrama)
    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    private String cpf;

    @NotBlank(message = "O login não pode estar em branco")
    @Size(max = 50, message = "O login deve ter no máximo 50 caracteres")
    private String login;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    private Double taxaProdutividade; // Double para taxaProdutividade

    @NotNull(message = "O cargo não pode ser nulo")
    private Cargo cargo; // Usando o enum Cargo

    @NotNull(message = "A data de contratação não pode ser nula")
    private LocalDate dataContratacao; // LocalDate para dataContratacao

    private Double salario;

    @NotBlank(message = "O horário de jornada não pode estar em branco")
    private String horarioJornada; // Simplificado para String por enquanto

    private Boolean eAdm; // boolean para eAdm

    @NotBlank(message = "A filial não pode estar em branco")
    @Size(max = 100, message = "A filial deve ter no máximo 100 caracteres")
    private String filial;

    @NotBlank(message = "O dispositivo chave não pode estar em branco")
    @Size(max = 50, message = "O dispositivo chave deve ter no máximo 50 caracteres")
    private String dispositivoChave;


}