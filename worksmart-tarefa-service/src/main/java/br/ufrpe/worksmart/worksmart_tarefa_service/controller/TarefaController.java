package br.ufrpe.worksmart.worksmart_tarefa_service.controller;

import br.ufrpe.worksmart.worksmart_tarefa_service.model.Tarefa;
import br.ufrpe.worksmart.worksmart_tarefa_service.model.Construcao;
import br.ufrpe.worksmart.worksmart_tarefa_service.model.Viagem;
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import br.ufrpe.worksmart.worksmart_tarefa_service.services.TarefaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// --- DTOs para Requisições POST/PUT ---

// DTO para atribuir uma TAREFA DE CONSTRUÇÃO
@Data
@NoArgsConstructor
@AllArgsConstructor
class AtribuirConstrucaoRequest {
    @NotBlank(message = "O título não pode estar em branco")
    private String titulo;
    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricaoTarefa;
    @NotNull(message = "A data para conclusão não pode ser nula")
    private LocalDate dataParaConclusao;
    @NotNull(message = "O ID do atribuidor não pode ser nulo")
    private Long idAtribuidor;
    @NotNull(message = "Os IDs dos executores não podem ser nulos e deve haver pelo menos um")
    @Size(min = 1, message = "A tarefa deve ter pelo menos um executor")
    private List<Long> idsExecutores;
    @NotBlank(message = "O tipo de construção não pode estar em branco")
    private String tipoConstrucao;
    private String enderecoTexto;
    private Map<String, Double> materiais;
}

// DTO para atribuir uma TAREFA DE VIAGEM
@Data
@NoArgsConstructor
@AllArgsConstructor
class AtribuirViagemRequest {
    @NotBlank(message = "O título não pode estar em branco")
    private String titulo;
    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricaoTarefa;
    @NotNull(message = "A data para conclusão não pode ser nula")
    private LocalDate dataParaConclusao;
    @NotNull(message = "O ID do atribuidor não pode ser nulo")
    private Long idAtribuidor;
    @NotNull(message = "Os IDs dos executores não podem ser nulos e deve haver pelo menos um")
    @Size(min = 1, message = "A tarefa deve ter pelo menos um executor")
    private List<Long> idsExecutores;
    @NotBlank(message = "O local de partida não pode estar em branco")
    private String localPartida;
    @NotBlank(message = "O local de destino não pode estar em branco")
    private String localDestino;
    @NotNull(message = "A hora de partida não pode ser nula")
    private LocalTime horaPartida;
    private LocalTime horaChegada;
    @NotBlank(message = "O meio de transporte não pode estar em branco")
    private String meioTransporte;
    private Double preco;
    private Double kmRodado;
    private Map<String, Double> materiaisTransportados;
}

// DTO para atualizar estado da tarefa (permanece o mesmo)
@Data
@NoArgsConstructor
@AllArgsConstructor
class AtualizarEstadoTarefaRequest {
    @NotNull(message = "O novo estado da tarefa não pode ser nulo")
    private EstadoTarefa novoEstado;
    @Min(value = 0, message = "A nota mínima é 0.0")
    @Max(value = 10, message = "A nota máxima é 10.0")
    private Float nota;
}


@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    @Autowired
    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    // Endpoint para atribuir uma TAREFA DE CONSTRUÇÃO (POST /api/tarefas/construcao)
    @PostMapping("/construcao")
    public ResponseEntity<Construcao> atribuirConstrucao(@Valid @RequestBody AtribuirConstrucaoRequest request) {
        try {
            Construcao novaConstrucao = tarefaService.atribuirConstrucao(
                    request.getTitulo(),
                    request.getDescricaoTarefa(),
                    request.getDataParaConclusao(),
                    request.getIdAtribuidor(),
                    request.getIdsExecutores(),
                    request.getTipoConstrucao(),
                    request.getEnderecoTexto(),
                    request.getMateriais()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConstrucao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para atribuir uma TAREFA DE VIAGEM (POST /api/tarefas/viagem)
    @PostMapping("/viagem")
    public ResponseEntity<Viagem> atribuirViagem(@Valid @RequestBody AtribuirViagemRequest request) {
        try {
            Viagem novaViagem = tarefaService.atribuirViagem(
                    request.getTitulo(),
                    request.getDescricaoTarefa(),
                    request.getDataParaConclusao(),
                    request.getIdAtribuidor(),
                    request.getIdsExecutores(),
                    request.getLocalPartida(),
                    request.getLocalDestino(),
                    request.getHoraPartida(),
                    request.getHoraChegada(),
                    request.getMeioTransporte(),
                    request.getPreco(),
                    request.getKmRodado(),
                    request.getMateriaisTransportados()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novaViagem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para atualizar o estado de uma tarefa (PUT /api/tarefas/{id}/estado)
    // Este endpoint é genérico e funciona para qualquer tipo de Tarefa (Construcao, Viagem)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Tarefa> atualizarEstadoTarefa(@PathVariable("id") Long idTarefa,
                                                        @Valid @RequestBody AtualizarEstadoTarefaRequest request) {
        try {
            Optional<Tarefa> tarefaAtualizada = tarefaService.atualizarEstadoTarefa(
                    idTarefa,
                    request.getNovoEstado(),
                    request.getNota()
            );
            return tarefaAtualizada
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para listar todas as tarefas (GET /api/tarefas)
    // Retorna todos os tipos de Tarefa (Construcao, Viagem)
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodasTarefas() {
        List<Tarefa> tarefas = tarefaService.listarTodasTarefas();
        if (tarefas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarefas);
    }

    // Endpoint para listar tarefas de um funcionário (GET /api/tarefas/funcionario/{idFuncionario})
    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<List<Tarefa>> listarTarefasPorFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            List<Tarefa> tarefas = tarefaService.listarTarefasPorFuncionario(idFuncionario);
            if (tarefas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tarefas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para listar tarefas pendentes (GET /api/tarefas/pendentes)
    @GetMapping("/pendentes")
    public ResponseEntity<List<Tarefa>> listarTarefasPendentes() {
        List<Tarefa> tarefas = tarefaService.listarTarefasPendentes();
        if (tarefas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarefas);
    }

    // Endpoint para listar tarefas concluídas fora do prazo (GET /api/tarefas/fora-prazo)
    @GetMapping("/fora-prazo")
    public ResponseEntity<List<Tarefa>> listarTarefasConcluidasForaDoPrazo() {
        List<Tarefa> tarefas = tarefaService.listarTarefasConcluidasForaDoPrazo();
        if (tarefas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarefas);
    }

    // Endpoint para deletar uma tarefa (DELETE /api/tarefas/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable("id") Long idTarefa) {
        try {
            tarefaService.deletarTarefa(idTarefa);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}