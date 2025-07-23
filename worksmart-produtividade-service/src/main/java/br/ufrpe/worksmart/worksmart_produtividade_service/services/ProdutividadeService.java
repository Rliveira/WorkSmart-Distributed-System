package br.ufrpe.worksmart.worksmart_produtividade_service.services;

// Importar os clientes de serviço
import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.RegistroDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.TarefaDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.client.FrequenciaServiceClient;
import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.FuncionarioDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.client.TarefaServiceClient;
import br.ufrpe.worksmart.worksmart_produtividade_service.client.UsuarioServiceClient;
import br.ufrpe.worksmart.worksmart_produtividade_service.exceptions.ServicoIndisponivelException;
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

@Service
public class ProdutividadeService {

    private final UsuarioServiceClient usuarioServiceClient;
    private final FrequenciaServiceClient frequenciaServiceClient;
    private final TarefaServiceClient tarefaServiceClient;

    @Autowired // Injeção de dependências dos clientes de serviço
    public ProdutividadeService(UsuarioServiceClient usuarioServiceClient,
                                FrequenciaServiceClient frequenciaServiceClient,
                                TarefaServiceClient tarefaServiceClient) {
        this.usuarioServiceClient = usuarioServiceClient;
        this.frequenciaServiceClient = frequenciaServiceClient;
        this.tarefaServiceClient = tarefaServiceClient;
    }

    // --- Funções de Cálculo (Traduzidas do Protótipo Haskell) ---

    // Metodo para normalizar um valor para uma escala (0 a 1, ou 0 a 100 depois)
    private Float normalizar(Number valor, Double maximo) {
        if (maximo == 0 || valor == null) return 0.0f;
        return (float) (valor.doubleValue() / maximo);
    }

    // Calcula o total de horas trabalhadas por um funcionário
    public Float calcularHorasTrabalhadas(Long idFuncionario) {
        List<RegistroDTO> registros = frequenciaServiceClient.listarRegistrosPorFuncionario(idFuncionario);
        return (float) registros.stream()
                .filter(r -> r.getHoraSaida() != null && r.getHoraEntrada() != null && r.getDataRegistro() != null)
                .mapToDouble(r -> {
                    LocalDateTime entrada = LocalDateTime.of(r.getDataRegistro(), r.getHoraEntrada());
                    LocalDateTime saida = LocalDateTime.of(r.getDataRegistro(), r.getHoraSaida());
                    return Duration.between(entrada, saida).toHours(); // Horas inteiras
                })
                .sum();
    }

    // Calcula a quantidade de tarefas realizadas (concluídas ou não realizadas) por um funcionário
    public Integer calcularQuantidadeTarefasRealizadas(Long idFuncionario) {
        List<TarefaDTO> tarefas = tarefaServiceClient.listarTarefasPorFuncionario(idFuncionario);
        return (int) tarefas.stream()
                .filter(t -> (t.getEstadoTarefa() == EstadoTarefa.CONCLUIDA || t.getEstadoTarefa() == EstadoTarefa.NAO_REALIZADA)
                        && t.getDataDeEfetivacao() != null
                        && t.getIdsExecutores() != null
                        && t.getIdsExecutores().contains(idFuncionario))
                .count();
    }

    // Calcula a média das notas das tarefas concluídas de um funcionário
    public Float calcularMediaNotas(Long idFuncionario) {
        List<TarefaDTO> tarefas = tarefaServiceClient.listarTarefasPorFuncionario(idFuncionario);
        List<Float> notas = tarefas.stream()
                .filter(t -> t.getEstadoTarefa() == EstadoTarefa.CONCLUIDA && t.getNota() != null)
                .map(TarefaDTO::getNota)
                .collect(Collectors.toList());
        if (notas.isEmpty()) {
            return 0.0f;
        }
        return (float) (notas.stream().mapToDouble(Float::doubleValue).average().orElse(0.0));
    }

    // Função para calcular o ajuste pelo prazo em uma tarefa (Traduzido do Haskell)
    public Float calcularAjustePrazo(Long idTarefa) {
        return tarefaServiceClient.buscarTarefaPorId(idTarefa)
                .map(tarefa -> {
                    if (tarefa.getDataDeEfetivacao() == null) {
                        return 0.0f; // Tarefa não concluída
                    }
                    long diasDeDiferenca = ChronoUnit.DAYS.between(tarefa.getDataParaConclusao(), tarefa.getDataDeEfetivacao());

                    if (diasDeDiferenca < 0) { // Concluído ANTES do prazo
                        return (float) Math.abs(diasDeDiferenca); // Bônus: +1 por dia adiantado
                    } else if (diasDeDiferenca > 0) { // Concluído DEPOIS do prazo (atrasado)
                        return -(float) diasDeDiferenca; // Penalidade: -1 por dia atrasado
                    } else { // Concluído NO prazo
                        return 0.0f;
                    }
                })
                .orElse(0.0f); // Se a tarefa não for encontrada ou erro
    }

    // Pesos para cada critério de produtividade (do protótipo Haskell)
    private static final float PESO_TAREFAS_CONCLUIDAS = 0.4f;
    private static final float PESO_NOTA = 0.3f;
    private static final float PESO_AJUSTE_PRAZO = 0.1f;
    private static final float PESO_HORAS_TRABALHADAS = 0.2f;

    // Calcular a produtividade geral de um funcionário
    // Retorna um valor de 0 a 100
    public Float calcularProdutividade(Long idFuncionario) throws IllegalArgumentException, ServicoIndisponivelException {

        Optional<FuncionarioDTO> funcionarioOpt = usuarioServiceClient.getFuncionarioDetails(idFuncionario);
        if (funcionarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Funcionário com ID " + idFuncionario + " não encontrado.");
        }

        Float horasTrabalhadas = calcularHorasTrabalhadas(idFuncionario); // Pode lançar ServicoIndisponivelException
        Integer tarefasConcluidas = calcularQuantidadeTarefasRealizadas(idFuncionario); // Pode lançar ServicoIndisponivelException
        Float mediaNotas = calcularMediaNotas(idFuncionario); // Pode lançar ServicoIndisponivelException

        Float ajustePrazoTotal = tarefaServiceClient.listarTarefasPorFuncionario(idFuncionario)
                .stream()
                .filter(t -> t.getDataDeEfetivacao() != null)
                .map(t -> {
                    try {
                        return calcularAjustePrazo(t.getId()); // Pode lançar ServicoIndisponivelException
                    } catch (ServicoIndisponivelException e) {
                        System.err.println("Erro ao calcular ajuste de prazo para tarefa " + t.getId() + ": " + e.getMessage());
                        return 0.0f; // Em caso de erro na tarefa, considera 0.0f para o ajuste
                    }
                })
                .reduce(0.0f, Float::sum);

        double maxHorasTrabalhadas = 160.0;
        double maxTarefasRealizadas = 10.0;
        double maxMediaNotas = 10.0;
        double maxAjustePrazoTotal = 30.0;

        Float horasNormalizadas = normalizar(horasTrabalhadas, maxHorasTrabalhadas);
        Float tarefasNormalizadas = normalizar(tarefasConcluidas, maxTarefasRealizadas);
        Float notasNormalizadas = normalizar(mediaNotas, maxMediaNotas);
        Float ajusteNormalizado = normalizar(ajustePrazoTotal, maxAjustePrazoTotal);

        Float produtividade = (PESO_HORAS_TRABALHADAS * horasNormalizadas +
                PESO_TAREFAS_CONCLUIDAS * tarefasNormalizadas +
                PESO_NOTA * notasNormalizadas +
                PESO_AJUSTE_PRAZO * ajusteNormalizado);

        return produtividade * 100;
    }

    // --- DTO para o Ranking ---
    // Esta classe interna será o formato dos objetos retornados pelos endpoints de ranking
    // Usamos Lombok @Data, @NoArgsConstructor, mas construtores manuais para flexibilidade
    @Data
    @NoArgsConstructor
    public static class ProdutividadeFuncionarioDTO {
        private Long idFuncionario;
        private String nomeFuncionario;
        private Float produtividadeGeral;
        private Float produtividadeDiaria; // Futuro: Requer busca de registros/tarefas filtrada por dia
        private Float produtividadeSemanal; // Futuro: Requer busca de registros/tarefas filtrada por semana
        private Float produtividadeMensal; // Futuro: Requer busca de registros/tarefas filtrada por mês
        private Integer tarefasConcluidas;
        private Float mediaNotas;
        private Float horasTrabalhadas;

        // Construtor completo
        public ProdutividadeFuncionarioDTO(Long idFuncionario, String nomeFuncionario,
                                           Float produtividadeGeral, Float produtividadeDiaria,
                                           Float produtividadeSemanal, Float produtividadeMensal,
                                           Integer tarefasConcluidas, Float mediaNotas, Float horasTrabalhadas) {
            this.idFuncionario = idFuncionario;
            this.nomeFuncionario = nomeFuncionario;
            this.produtividadeGeral = produtividadeGeral;
            this.produtividadeDiaria = produtividadeDiaria;
            this.produtividadeSemanal = produtividadeSemanal;
            this.produtividadeMensal = produtividadeMensal;
            this.tarefasConcluidas = tarefasConcluidas;
            this.mediaNotas = mediaNotas;
            this.horasTrabalhadas = horasTrabalhadas;
        }

        // Construtor simplificado para Ranking de Tarefas Concluidas (usa 0.0f para produtividades)
        public ProdutividadeFuncionarioDTO(Long idFuncionario, String nomeFuncionario, Integer tarefasConcluidas) {
            this(idFuncionario, nomeFuncionario, 0.0f, 0.0f, 0.0f, 0.0f, tarefasConcluidas, 0.0f, 0.0f);
        }

        // Construtor simplificado para Ranking por Notas (usa 0.0f para produtividades e tarefas)
        public ProdutividadeFuncionarioDTO(Long idFuncionario, String nomeFuncionario, Float mediaNotas) {
            this(idFuncionario, nomeFuncionario, 0.0f, 0.0f, 0.0f, 0.0f, 0, mediaNotas, 0.0f);
        }
    }

    // --- Métodos de Ranking (Traduzidos do Protótipo Haskell) ---

    public List<ProdutividadeFuncionarioDTO> rankingPorProdutividade() throws ServicoIndisponivelException {
        // A exceção é propagada por usuarioServiceClient.listarTodosFuncionarios()
        List<FuncionarioDTO> todosFuncionarios = usuarioServiceClient.listarTodosFuncionarios();
        return todosFuncionarios.stream()
                .map(f -> {
                    try {
                        Float produtividadeGeral = calcularProdutividade(f.getId()); // Pode lançar ServicoIndisponivelException
                        Float horasTrabalhadas = calcularHorasTrabalhadas(f.getId());
                        Integer tarefasConcluidas = calcularQuantidadeTarefasRealizadas(f.getId());
                        Float mediaNotas = calcularMediaNotas(f.getId());

                        return new ProdutividadeFuncionarioDTO(
                                f.getId(),
                                f.getNome(),
                                produtividadeGeral,
                                0.0f, // TODO: Implementar Produtividade Diária no futuro
                                0.0f, // TODO: Implementar Produtividade Semanal no futuro
                                0.0f, // TODO: Implementar Produtividade Mensal no futuro
                                tarefasConcluidas,
                                mediaNotas,
                                horasTrabalhadas
                        );
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao calcular produtividade para funcionário " + f.getId() + ": " + e.getMessage());
                        return null; // Retorna nulo para filtrar depois
                    } catch (ServicoIndisponivelException e) { // Captura a exceção de serviço indisponível
                        System.err.println("Erro ao buscar dados de serviço dependente para ranking do funcionário " + f.getId() + ": " + e.getMessage());
                        return null; // Retorna nulo para filtrar depois
                    }
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(ProdutividadeFuncionarioDTO::getProdutividadeGeral, Comparator.nullsLast(Float::compareTo)).reversed())
                .collect(Collectors.toList());
    }

    public List<ProdutividadeFuncionarioDTO> rankingPorTarefasConcluidas() throws ServicoIndisponivelException {
        List<FuncionarioDTO> todosFuncionarios = usuarioServiceClient.listarTodosFuncionarios();
        return todosFuncionarios.stream()
                .map(f -> {
                    try {
                        return new ProdutividadeFuncionarioDTO(f.getId(), f.getNome(), calcularQuantidadeTarefasRealizadas(f.getId()));
                    } catch (ServicoIndisponivelException e) {
                        System.err.println("Erro ao calcular tarefas concluídas para funcionário " + f.getId() + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(ProdutividadeFuncionarioDTO::getTarefasConcluidas, Comparator.nullsLast(Integer::compareTo)).reversed())
                .collect(Collectors.toList());
    }
    public List<ProdutividadeFuncionarioDTO> rankingPorNotas() throws ServicoIndisponivelException {
        List<FuncionarioDTO> todosFuncionarios = usuarioServiceClient.listarTodosFuncionarios();
        return todosFuncionarios.stream()
                .map(f -> {
                    try {
                        return new ProdutividadeFuncionarioDTO(f.getId(), f.getNome(), calcularMediaNotas(f.getId()));
                    } catch (ServicoIndisponivelException e) {
                        System.err.println("Erro ao calcular média de notas para funcionário " + f.getId() + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(ProdutividadeFuncionarioDTO::getMediaNotas, Comparator.nullsLast(Float::compareTo)).reversed())
                .collect(Collectors.toList());
    }


    // TODO: Adicionar rankingPorFrequencia aqui, utilizando o frequenciaServiceClient
    // e a lógica do protótipo Haskell para calcular a frequência/presença.
}