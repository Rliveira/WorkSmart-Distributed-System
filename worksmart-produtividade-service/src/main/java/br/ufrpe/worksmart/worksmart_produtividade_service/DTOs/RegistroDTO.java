package br.ufrpe.worksmart.worksmart_produtividade_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private Long id;
    private Long idFuncionario;
    private LocalDate dataRegistro;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
}
