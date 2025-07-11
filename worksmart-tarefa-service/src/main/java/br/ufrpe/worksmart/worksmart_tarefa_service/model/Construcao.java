// worksmart-tarefa-service/src/main/java/br/ufrpe/worksmart/tarefa/model/Construcao.java
package br.ufrpe.worksmart.worksmart_tarefa_service.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Importante: callSuper=true para incluir atributos da classe pai (Tarefa)
public class Construcao extends Tarefa { // EXTENDE Tarefa

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O tipo de construção não pode estar em branco")
    @Size(max = 100, message = "O tipo de construção deve ter no máximo 100 caracteres")
    private String tipoConstrucao;

    private String enderecoTexto;

    @ElementCollection
    @CollectionTable(name = "construcao_materiais", joinColumns = @JoinColumn(name = "construcao_id"))
    @Column(name = "quantidade_material")
    private Map<String, Double> materiais;

    // Construtor completo para Construcao
    public Construcao(String titulo, String descricaoTarefa, LocalDate dataAtribuicao,
                      LocalDate dataParaConclusao, Long idAtribuidor, List<Long> idsExecutores,
                      String tipoConstrucao, String enderecoTexto, Map<String, Double> materiais) {
        // Chama o construtor da classe pai 'Tarefa'
        super(titulo, descricaoTarefa, dataAtribuicao, dataParaConclusao, idAtribuidor, idsExecutores);
        this.tipoConstrucao = tipoConstrucao;
        this.enderecoTexto = enderecoTexto;
        this.materiais = materiais;
    }
}