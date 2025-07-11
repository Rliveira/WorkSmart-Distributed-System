// worksmart-tarefa-service/src/main/java/br/ufrpe/worksmart/tarefa/model/Viagem.java
package br.ufrpe.worksmart.worksmart_tarefa_service.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Viagem extends Tarefa { // EXTENDE Tarefa

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O local de partida não pode estar em branco")
    @Size(max = 100, message = "O local de partida deve ter no máximo 100 caracteres")
    private String localPartida;

    @NotBlank(message = "O local de destino não pode estar em branco")
    @Size(max = 100, message = "O local de destino deve ter no máximo 100 caracteres")
    private String localDestino;

    @NotNull(message = "A hora de partida não pode ser nula")
    private LocalTime horaPartida;

    private LocalTime horaChegada;

    @NotBlank(message = "O meio de transporte não pode estar em branco")
    @Size(max = 50, message = "O meio de transporte deve ter no máximo 50 caracteres")
    private String meioTransporte;

    private Double preco;

    private Double kmRodado;

    @ElementCollection
    @CollectionTable(name = "viagem_materiais", joinColumns = @JoinColumn(name = "viagem_id"))
    @Column(name = "quantidade_material")
    private Map<String, Double> materiaisTransportados;

    // Construtor completo para Viagem
    public Viagem(String titulo, String descricaoTarefa, LocalDate dataAtribuicao,
                  LocalDate dataParaConclusao, Long idAtribuidor, List<Long> idsExecutores,
                  String localPartida, String localDestino, LocalTime horaPartida,
                  LocalTime horaChegada, String meioTransporte, Double preco, Double kmRodado,
                  Map<String, Double> materiaisTransportados) {
        super(titulo, descricaoTarefa, dataAtribuicao, dataParaConclusao, idAtribuidor, idsExecutores);
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