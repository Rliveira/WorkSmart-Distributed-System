package br.ufrpe.worksmart.worksmart_frequencia_service.repository;

import br.ufrpe.worksmart.worksmart_frequencia_service.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    // Método para encontrar um registro de entrada para um funcionário em uma dada data, sem hora de saída
    Optional<Registro> findByIdFuncionarioAndDataRegistroAndHoraSaidaIsNull(Long idFuncionario, LocalDate dataRegistro);

    // Método para listar todos os registros de um funcionário
    List<Registro> findByIdFuncionario(Long idFuncionario);

    // Método para listar registros em um período específico (útil para relatórios)
    List<Registro> findByDataRegistroBetween(LocalDate startDate, LocalDate endDate);
}