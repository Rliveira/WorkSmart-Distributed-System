package br.ufrpe.worksmart.worksmart_usuario_service.model; // Pacote do seu novo serviço de usuário

import br.ufrpe.worksmart.worksmart_usuario_service.model.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor; // Este AllArgsConstructor pode ser tricky com herança, vamos ajustá-lo
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

// Mantenha o Enum Cargo aqui ou em um pacote 'enums' se quiser
enum Cargo {
    DESENVOLVEDOR, GERENTE, ANALISTA, SUPORTE, OUTRO
}

@Entity
@EqualsAndHashCode(callSuper = true) // MUDE para TRUE para incluir atributos da classe pai (Usuario) no equals/hashCode
@Data
@NoArgsConstructor
// REMOVA @AllArgsConstructor para Funcionario, e crie um construtor manual se precisar de um com todos os campos (incluindo pai)
public class Funcionario extends Usuario implements Serializable { // AGORA EXTENDE Usuario

    private static final long serialVersionUID = 1L;

    // Atributos específicos de Funcionario
    private Double taxaProdutividade;

    @NotNull(message = "O cargo não pode ser nulo")
    @Enumerated(EnumType.STRING) // Armazena o nome do enum como String no DB
    private Cargo cargo;

    @NotNull(message = "A data de contratação não pode ser nula")
    private LocalDate dataContratacao;

    private Double salario;

    @NotBlank(message = "O horário de jornada não pode estar em branco")
    private String horarioJornada;

    private Boolean eAdm;

    @NotBlank(message = "A filial não pode estar em branco")
    @Size(max = 100, message = "A filial deve ter no máximo 100 caracteres")
    private String filial;

    @NotBlank(message = "O dispositivo chave não pode estar em branco")
    @Size(max = 50, message = "O dispositivo chave deve ter no máximo 50 caracteres")
    private String dispositivoChave;

    // Exemplo de construtor completo que chama o construtor do pai
    public Funcionario(Long id, String nome, String cpf, String login, String senha,
                       Double taxaProdutividade, Cargo cargo, LocalDate dataContratacao,
                       Double salario, String horarioJornada, Boolean eAdm, String filial, String dispositivoChave) {
        super(id, nome, cpf, login, senha); // Chama o construtor da classe Usuario
        this.taxaProdutividade = taxaProdutividade;
        this.cargo = cargo;
        this.dataContratacao = dataContratacao;
        this.salario = salario;
        this.horarioJornada = horarioJornada;
        this.eAdm = eAdm;
        this.filial = filial;
        this.dispositivoChave = dispositivoChave;
    }
}