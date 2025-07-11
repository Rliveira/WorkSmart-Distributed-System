package br.ufrpe.worksmart.worksmart_usuario_service.controllers; // Pacote do seu novo serviço de usuário

import br.ufrpe.worksmart.worksmart_usuario_service.model.Funcionario; // Importar Funcionario
import br.ufrpe.worksmart.worksmart_usuario_service.services.FuncionarioService; // Importar FuncionarioService
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios") // Este endpoint agora trata de Funcionarios dentro do serviço de usuário
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<Funcionario> criarFuncionario(@Valid @RequestBody Funcionario funcionario) {
        Funcionario novoFuncionario = funcionarioService.salvarFuncionario(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodosFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.listarTodosFuncionarios();
        if (funcionarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarFuncionarioPorId(@PathVariable Long id) {
        return funcionarioService.buscarFuncionarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizarFuncionario(@PathVariable Long id, @Valid @RequestBody Funcionario funcionarioAtualizado) {
        return funcionarioService.atualizarFuncionario(id, funcionarioAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        if (funcionarioService.buscarFuncionarioPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tempo-servico")
    public ResponseEntity<Double> calcularTempoDeServico(@PathVariable Long id) {
        Double tempoDeServico = funcionarioService.calcularTempoDeServico(id);
        if (tempoDeServico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tempoDeServico);
    }

    @GetMapping("/{id}/taxa-produtividade")
    public ResponseEntity<Double> calcularTaxaProdutividade(@PathVariable Long id) {
        Double taxaProdutividade = funcionarioService.calcularTaxaProdutividade(id);
        if (taxaProdutividade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taxaProdutividade);
    }
}