package br.ufrpe.worksmart.worksmart_produtividade_service.controllers;

import br.ufrpe.worksmart.worksmart_produtividade_service.services.ProdutividadeService;
import br.ufrpe.worksmart.worksmart_produtividade_service.services.ProdutividadeService.ProdutividadeFuncionarioDTO; // Importar a classe interna
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marca esta classe como um controlador REST
@RequestMapping("/api/produtividade") // Define o caminho base para todos os endpoints deste controlador
public class ProdutividadeController {

    private final ProdutividadeService produtividadeService;

    @Autowired // Injeta a dependência do serviço
    public ProdutividadeController(ProdutividadeService produtividadeService) {
        this.produtividadeService = produtividadeService;
    }

    // Endpoint para calcular a produtividade geral de um funcionário
    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<Float> calcularProdutividadeFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            Float produtividade = produtividadeService.calcularProdutividade(idFuncionario);
            return ResponseEntity.ok(produtividade);
        } catch (IllegalArgumentException e) {
            // Captura exceções de argumento inválido
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
        } catch (Exception e) {
            // Captura qualquer outro erro inesperado no servidor
            System.err.println("Erro inesperado ao calcular produtividade para ID " + idFuncionario + ": " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna 500 Internal Server Error
        }
    }

    // Endpoint para Ranking por Produtividade Geral
    @GetMapping("/ranking/geral")
    public ResponseEntity<List<ProdutividadeFuncionarioDTO>> rankingPorProdutividade() {
        List<ProdutividadeFuncionarioDTO> ranking = produtividadeService.rankingPorProdutividade();
        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a lista estiver vazia
        }
        return ResponseEntity.ok(ranking); // Retorna 200 OK com a lista de ranking
    }

    // Endpoint para Ranking por Tarefas Concluídas (GET /api/produtividade/ranking/tarefas-concluidas)
    @GetMapping("/ranking/tarefas-concluidas")
    public ResponseEntity<List<ProdutividadeFuncionarioDTO>> rankingPorTarefasConcluidas() {
        List<ProdutividadeFuncionarioDTO> ranking = produtividadeService.rankingPorTarefasConcluidas();
        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ranking);
    }

    // Endpoint para Ranking por Notas (GET /api/produtividade/ranking/notas)
    @GetMapping("/ranking/notas")
    public ResponseEntity<List<ProdutividadeFuncionarioDTO>> rankingPorNotas() {
        List<ProdutividadeFuncionarioDTO> ranking = produtividadeService.rankingPorNotas();
        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ranking);
    }

}