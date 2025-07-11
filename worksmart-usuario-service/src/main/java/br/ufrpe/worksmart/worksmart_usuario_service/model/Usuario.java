package br.ufrpe.worksmart.worksmart_usuario_service.model; // Pacote do seu novo serviço de usuário

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType; // Para herança JPA
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity // Marca como entidade JPA
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de herança para subclasses (Funcionario, Cliente, Fornecedor)
@Data // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: construtor sem argumentos
@AllArgsConstructor // Lombok: construtor com todos os argumentos
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    // @Column(unique = true) // Descomente se o CPF deve ser único no DB
    private String cpf;

    @NotBlank(message = "O login não pode estar em branco")
    @Size(max = 50, message = "O login deve ter no máximo 50 caracteres")
    // @Column(unique = true) // Descomente se o login deve ser único no DB
    private String login;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;
}