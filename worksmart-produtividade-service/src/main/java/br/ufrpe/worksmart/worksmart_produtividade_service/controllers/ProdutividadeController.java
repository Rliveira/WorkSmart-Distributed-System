package br.ufrpe.worksmart.worksmart_produtividade_service.controllers;

import br.ufrpe.worksmart.worksmart_produtividade_service.services.ProdutividadeService;
import br.ufrpe.worksmart.worksmart_produtividade_service.services.ProdutividadeService.ProdutividadeFuncionarioDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.exceptions.ServicoIndisponivelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/produtividade") // Define o caminho base para todos os endpoints deste controlador
public class ProdutividadeController {


    private static final Logger logger = LoggerFactory.getLogger(ProdutividadeController.class);

    private final ProdutividadeService produtividadeService;

    @Autowired
    public ProdutividadeController(ProdutividadeService produtividadeService) {
        this.produtividadeService = produtividadeService;
    }

    // Handler para capturar ServicoIndisponivelException e retornar uma resposta HTTP 503
    @ExceptionHandler(ServicoIndisponivelException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // Define o status HTTP para 503 Service Unavailable
    public ResponseEntity<String> handleServicoIndisponivelException(ServicoIndisponivelException ex) {
        logger.error("Falha de comunicação com serviço dependente: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Erro de comunicação: " + ex.getMessage());
    }

    // Endpoint para calcular a produtividade geral de um funcionário
    // GET /api/produtividade/funcionario/{idFuncionario}
    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<Float> calcularProdutividadeFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            Float produtividade = produtividadeService.calcularProdutividade(idFuncionario);
            return ResponseEntity.ok(produtividade); // Retorna 200 OK com o valor da produtividade
        } catch (IllegalArgumentException e) {
            // Captura exceções de argumento inválido
            logger.warn("Requisição inválida para calcular produtividade do funcionário {}: {}", idFuncionario, e.getMessage());
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
        } catch (Exception e) {
            logger.error("Erro inesperado ao calcular produtividade para ID {}: {}", idFuncionario, e.getMessage(), e); // Loga o stack trace completo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna 500 Internal Server Error
        }
    }

    // Endpoint para Ranking por Produtividade Geral
    // GET /api/produtividade/ranking/geral
    @GetMapping("/ranking/geral")
    public ResponseEntity<List<ProdutividadeFuncionarioDTO>> rankingPorProdutividade() {
        try {
            List<ProdutividadeFuncionarioDTO> ranking = produtividadeService.rankingPorProdutividade();
            if (ranking.isEmpty()) {
                return ResponseEntity.noContent().build(); // Retorna 204 No Content se a lista estiver vazia
            }
            return ResponseEntity.ok(ranking); // Retorna 200 OK com a lista de ranking
        } catch (Exception e) {
            logger.error("Erro inesperado ao gerar ranking geral: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para Ranking por Tarefas Concluídas
    // GET /api/produtividade/ranking/tarefas-concluidas
    @GetMapping("/ranking/tarefas-concluidas")
    public ResponseEntity<List<ProdutividadeFuncionarioDTO>> rankingPorTarefasConcluidas() {
        try {
            List<ProdutividadeFuncionarioDTO> ranking = produtividadeService.rankingPorTarefasConcluidas();
            if (ranking.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            logger.error("Erro inesperado ao gerar ranking por tarefas concluídas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para Ranking por Notas
    // GET /api/produtividade/ranking/notas
    @GetMapping("/ranking/notas")
    public ResponseEntity<List<ProdutividadeFuncionarioDTO>> rankingPorNotas() {
        try {
            List<ProdutividadeFuncionarioDTO> ranking = produtividadeService.rankingPorNotas();
            if (ranking.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            logger.error("Erro inesperado ao gerar ranking por notas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}