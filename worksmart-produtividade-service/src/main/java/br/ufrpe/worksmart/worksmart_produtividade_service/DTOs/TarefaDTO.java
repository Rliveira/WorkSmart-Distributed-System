package br.ufrpe.worksmart.worksmart_produtividade_service.DTOs;

import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class TarefaDTO {
    private Long id;
    private String titulo;
    private Float nota;
    private String descricaoTarefa;
    private LocalDate dataAtribuicao;
    private LocalDate dataParaConclusao;
    private LocalDate dataDeEfetivacao;
    private EstadoTarefa estadoTarefa;
    private Long idAtribuidor;
    private List<Long> idsExecutores;
    private String type; // Campo discriminador para Jackson

}