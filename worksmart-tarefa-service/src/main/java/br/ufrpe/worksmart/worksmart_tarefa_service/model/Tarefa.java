
package br.ufrpe.worksmart.worksmart_tarefa_service.model;

import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity // Marca esta classe como uma entidade JPA
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de herança: tabelas separadas para pai e filhos
@Data // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: construtor sem argumentos
@EqualsAndHashCode(callSuper = false) // Nenhuma classe pai de entidade acima, então false
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usar o nome do tipo como discriminador
        include = JsonTypeInfo.As.PROPERTY, // A propriedade que discrimina estará no JSON
        property = "type" // Nome da propriedade no JSON que indicará o tipo (ex: "type": "Construcao")
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Construcao.class, name = "Construcao"), // Mapear para as ENTIDADES
        @JsonSubTypes.Type(value = Viagem.class, name = "Viagem")
})
public abstract class Tarefa implements Serializable { // AGORA É ABSTRATA!

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar em branco")
    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
    private String titulo;

    @Min(value = 0, message = "A nota mínima é 0.0")
    @Max(value = 10, message = "A nota máxima é 10.0")
    private Float nota; // Nota para a qualidade da execução da tarefa

    @NotBlank(message = "A descrição não pode estar em branco")
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String descricaoTarefa;

    @NotNull(message = "A data de atribuição não pode ser nula")
    private LocalDate dataAtribuicao; // Data em que a tarefa foi atribuída

    @NotNull(message = "A data para conclusão não pode ser nula")
    private LocalDate dataParaConclusao; // Prazo original

    private LocalDate dataDeEfetivacao; // Data real de conclusão (pode ser null se não concluída)

    @NotNull(message = "O estado da tarefa não pode ser nulo")
    @Enumerated(EnumType.STRING) // Armazena o nome do enum como String no DB
    private EstadoTarefa estadoTarefa;

    @NotNull(message = "O ID do atribuidor não pode ser nulo")
    private Long idAtribuidor; // ID do funcionário que atribuiu a tarefa

    @ElementCollection // Para coleções de tipos básicos
    @CollectionTable(name = "tarefa_executores", joinColumns = @JoinColumn(name = "tarefa_id"))
    @Column(name = "id_executor")
    @Size(min = 1, message = "A tarefa deve ter pelo menos um executor")
    private List<Long> idsExecutores = new ArrayList<>(); // IDs dos funcionários executores

    // Construtor protegido, a ser chamado pelas subclasses
    protected Tarefa(Long id, String titulo, Float nota, String descricaoTarefa,
                     LocalDate dataAtribuicao, LocalDate dataParaConclusao,
                     LocalDate dataDeEfetivacao, EstadoTarefa estadoTarefa,
                     Long idAtribuidor, List<Long> idsExecutores) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
        this.descricaoTarefa = descricaoTarefa;
        this.dataAtribuicao = dataAtribuicao;
        this.dataParaConclusao = dataParaConclusao;
        this.dataDeEfetivacao = dataDeEfetivacao;
        this.estadoTarefa = estadoTarefa;
        this.idAtribuidor = idAtribuidor;
        this.idsExecutores = idsExecutores;
        if (this.idsExecutores == null) {
            this.idsExecutores = new ArrayList<>();
        }
    }

    // Construtor básico para subclasses inicializarem com valores padrão
    protected Tarefa(String titulo, String descricaoTarefa, LocalDate dataAtribuicao, LocalDate dataParaConclusao,
                     Long idAtribuidor, List<Long> idsExecutores) {
        this(null, titulo, 0.0f, descricaoTarefa, dataAtribuicao, dataParaConclusao,
                null, EstadoTarefa.EM_PROGRESSO, idAtribuidor, idsExecutores);
    }
}