package br.ufrpe.worksmart.worksmart_frequencia_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate; // Para DataRegistro
import java.time.LocalTime; // Para HoraEntrada e HoraSaida

@Entity // Marca esta classe como uma entidade JPA
@Data // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: construtor sem argumentos
@AllArgsConstructor // Lombok: construtor com todos os argumentos
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente
    private Long id; // Corresponde ao IDRegistro do Haskell

    @NotNull(message = "O ID do funcionário não pode ser nulo")
    private Long idFuncionario; // Armazenamos apenas o ID do funcionário aqui

    @NotNull(message = "A data do registro não pode ser nula")
    private LocalDate dataRegistro;

    @NotNull(message = "A hora de entrada não pode ser nula")
    private LocalTime horaEntrada;

    private LocalTime horaSaida; // Pode ser nulo (Maybe Hora no Haskell)

    // Nota: O objeto Funcionario completo não é armazenado aqui.
    // Se precisarmos dos detalhes do funcionário, faremos uma requisição ao worksmart-usuario-service.
    // Isso demonstra a comunicação entre microsserviços.
}