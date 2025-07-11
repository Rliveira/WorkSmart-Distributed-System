package br.ufrpe.worksmart.worksmart_tarefa_service.services;

import br.ufrpe.worksmart.worksmart_tarefa_service.client.UsuarioServiceClient;
import br.ufrpe.worksmart.worksmart_tarefa_service.model.Tarefa;
import br.ufrpe.worksmart.worksmart_tarefa_service.model.Construcao; // Importar Construcao
import br.ufrpe.worksmart.worksmart_tarefa_service.model.Viagem; // Importar Viagem
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import br.ufrpe.worksmart.worksmart_tarefa_service.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioServiceClient usuarioServiceClient;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository, UsuarioServiceClient usuarioServiceClient) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioServiceClient = usuarioServiceClient;
    }

    // --- Métodos de Negócio para Atribuição de Tarefas (por tipo específico) ---

    // Método para atribuir uma TAREFA DE CONSTRUÇÃO
    public Construcao atribuirConstrucao(String titulo, String descricao, LocalDate dataParaConclusao,
                                         Long idAtribuidor, List<Long> idsExecutores,
                                         String tipoConstrucao, String enderecoTexto, Map<String, Double> materiais) {
        validarAtribuicaoBase(dataParaConclusao, idAtribuidor, idsExecutores);
        Construcao novaConstrucao = new Construcao(titulo, descricao, LocalDate.now(), dataParaConclusao,
                idAtribuidor, idsExecutores, tipoConstrucao, enderecoTexto, materiais);
        return tarefaRepository.save(novaConstrucao);
    }

    // Método para atribuir uma TAREFA DE VIAGEM
    public Viagem atribuirViagem(String titulo, String descricao, LocalDate dataParaConclusao,
                                 Long idAtribuidor, List<Long> idsExecutores,
                                 String localPartida, String localDestino, LocalTime horaPartida,
                                 LocalTime horaChegada, String meioTransporte, Double preco, Double kmRodado,
                                 Map<String, Double> materiaisTransportados) {
        validarAtribuicaoBase(dataParaConclusao, idAtribuidor, idsExecutores);
        Viagem novaViagem = new Viagem(titulo, descricao, LocalDate.now(), dataParaConclusao,
                idAtribuidor, idsExecutores, localPartida, localDestino,
                horaPartida, horaChegada, meioTransporte, preco, kmRodado, materiaisTransportados);
        return tarefaRepository.save(novaViagem);
    }

    // Método auxiliar para validações comuns na atribuição de qualquer tarefa
    private void validarAtribuicaoBase(LocalDate dataParaConclusao, Long idAtribuidor, List<Long> idsExecutores) {
        if (dataParaConclusao.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data para conclusão deve ser posterior à data atual.");
        }
        if (!usuarioServiceClient.funcionarioExiste(idAtribuidor)) {
            throw new IllegalArgumentException("Atribuidor com ID " + idAtribuidor + " não encontrado.");
        }
        if (idsExecutores == null || idsExecutores.isEmpty()) {
            throw new IllegalArgumentException("A tarefa deve ter pelo menos um executor.");
        }
        for (Long idExecutor : idsExecutores) {
            if (!usuarioServiceClient.funcionarioExiste(idExecutor)) {
                throw new IllegalArgumentException("Executor com ID " + idExecutor + " não encontrado.");
            }
        }
    }


    // --- Métodos de Listagem e Atualização (operam em Tarefa base, o polimorfismo é automático) ---

    public Optional<Tarefa> atualizarEstadoTarefa(Long idTarefa, EstadoTarefa novoEstado, Float nota) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(idTarefa);

        if (tarefaOpt.isEmpty()) {
            throw new IllegalArgumentException("Tarefa com ID " + idTarefa + " não encontrada.");
        }

        Tarefa tarefa = tarefaOpt.get();

        // Lógica para atualização do estado
        if (novoEstado == EstadoTarefa.CONCLUIDA) {
            if (nota == null || nota < 0 || nota > 10) {
                throw new IllegalArgumentException("Nota é obrigatória e deve estar entre 0.0 e 10.0 para tarefas concluídas.");
            }
            tarefa.setEstadoTarefa(EstadoTarefa.CONCLUIDA);
            tarefa.setNota(nota);
            tarefa.setDataDeEfetivacao(LocalDate.now());

            // Se a tarefa for concluída, verifica se está fora do prazo
            // Consistente com o protótipo Haskell: se concluída fora do prazo, vira "Não Realizada"
            if (tarefa.getDataDeEfetivacao().isAfter(tarefa.getDataParaConclusao())) {
                tarefa.setEstadoTarefa(EstadoTarefa.NAO_REALIZADA);
                System.out.println("Tarefa " + idTarefa + " concluída fora do prazo. Estado atualizado para Não Realizada.");
            }
        } else if (novoEstado == EstadoTarefa.NAO_REALIZADA) {
            tarefa.setEstadoTarefa(EstadoTarefa.NAO_REALIZADA);
            tarefa.setNota(0.0f); // Limpa a nota
            tarefa.setDataDeEfetivacao(LocalDate.now()); // Marca a data que foi identificada como não realizada
        } else if (novoEstado == EstadoTarefa.EM_PROGRESSO) {
            tarefa.setEstadoTarefa(EstadoTarefa.EM_PROGRESSO);
            tarefa.setNota(0.0f); // Limpa a nota
            tarefa.setDataDeEfetivacao(null); // Limpa a data de efetivação
        } else {
            throw new IllegalArgumentException("Estado da tarefa inválido.");
        }
        return Optional.of(tarefaRepository.save(tarefa));
    }


    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.findAll();
    }

    public List<Tarefa> listarTarefasPorFuncionario(Long idFuncionario) {
        if (!usuarioServiceClient.funcionarioExiste(idFuncionario)) {
            throw new IllegalArgumentException("Funcionário com ID " + idFuncionario + " não encontrado.");
        }
        return tarefaRepository.findByIdAtribuidorOrIdsExecutoresContaining(idFuncionario, idFuncionario);
    }

    public List<Tarefa> listarTarefasPendentes() {
        return tarefaRepository.findByEstadoTarefa(EstadoTarefa.EM_PROGRESSO);
    }

    public List<Tarefa> listarTarefasConcluidasForaDoPrazo() {
        return tarefaRepository.findByEstadoTarefa(EstadoTarefa.NAO_REALIZADA);
    }

    public void deletarTarefa(Long idTarefa) {
        if (!tarefaRepository.existsById(idTarefa)) {
            throw new IllegalArgumentException("Tarefa com ID " + idTarefa + " não encontrada para exclusão.");
        }
        tarefaRepository.deleteById(idTarefa);
    }
}