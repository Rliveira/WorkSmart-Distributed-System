package br.ufrpe.worksmart.worksmart_produtividade_service.DTOs;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.TarefaDTO;
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// DTO para Construcao (subclasse concreta de TarefaDTO)
@Data
@NoArgsConstructor
public class ConstrucaoDTO extends TarefaDTO {
    private String tipoConstrucao;
    private String enderecoTexto;
    private Map<String, Double> materiais;

    public ConstrucaoDTO(Long id, String titulo, Float nota, String descricaoTarefa,
                         LocalDate dataAtribuicao, LocalDate dataParaConclusao,
                         LocalDate dataDeEfetivacao, EstadoTarefa estadoTarefa,
                         Long idAtribuidor, List<Long> idsExecutores,
                         String tipoConstrucao, String enderecoTexto, Map<String, Double> materiais) {
        super(id, titulo, nota, descricaoTarefa, dataAtribuicao, dataParaConclusao,
                dataDeEfetivacao, estadoTarefa, idAtribuidor,null, "Construcao");
        this.tipoConstrucao = tipoConstrucao;
        this.enderecoTexto = enderecoTexto;
        this.materiais = materiais;
    }
}