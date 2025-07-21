package br.ufrpe.worksmart.worksmart_produtividade_service.DTOs;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.TarefaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

// DTO para Projeto (NÃO HERDA DE TarefaDTO) - Agregador de Tarefas
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String codigoProjeto;

    private LocalDate dataInicio;
    private LocalDate dataConclusaoEstimada;
    private LocalDate dataConclusaoReal;
    private String status;
    private Long idResponsavel;

    private List<TarefaDTO> tarefas; // Lista de Tarefas (polimórficas) que o projeto contém
}