package br.ufrpe.worksmart.worksmart_produtividade_service.DTOs;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.TarefaDTO;
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

// DTO para Viagem (subclasse concreta de TarefaDTO)
@Data
@NoArgsConstructor
public class ViagemDTO extends TarefaDTO {
    private String localPartida;
    private String localDestino;
    private LocalTime horaPartida;
    private LocalTime horaChegada;
    private String meioTransporte;
    private Double preco;
    private Double kmRodado;
    private Map<String, Double> materiaisTransportados;

    public ViagemDTO(Long id, String titulo, Float nota, String descricaoTarefa,
                     LocalDate dataAtribuicao, LocalDate dataParaConclusao,
                     LocalDate dataDeEfetivacao, EstadoTarefa estadoTarefa,
                     Long idAtribuidor, List<Long> idsExecutores,
                     String localPartida, String localDestino, LocalTime horaPartida,
                     LocalTime horaChegada, String meioTransporte, Double preco, Double kmRodado,
                     Map<String, Double> materiaisTransportados) {
        super(id, titulo, nota, descricaoTarefa, dataAtribuicao, dataParaConclusao,
                dataDeEfetivacao, estadoTarefa, idAtribuidor, idsExecutores, "Viagem");
        this.localPartida = localPartida;
        this.localDestino = localDestino;
        this.horaPartida = horaPartida;
        this.horaChegada = horaChegada;
        this.meioTransporte = meioTransporte;
        this.preco = preco;
        this.kmRodado = kmRodado;
        this.materiaisTransportados = materiaisTransportados;
    }
}