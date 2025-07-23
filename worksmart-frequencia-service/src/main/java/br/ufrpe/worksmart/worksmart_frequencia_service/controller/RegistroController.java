package br.ufrpe.worksmart.worksmart_frequencia_service.controller;

import br.ufrpe.worksmart.worksmart_frequencia_service.model.Registro;
import br.ufrpe.worksmart.worksmart_frequencia_service.services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros")
public class RegistroController {

    private final RegistroService registroService;

    @Autowired
    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/entrada/{idFuncionario}")
    public ResponseEntity<Registro> registrarEntrada(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            Registro novoRegistro = registroService.registrarEntrada(idFuncionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoRegistro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
        }
    }

    @PostMapping("/saida/{idFuncionario}")
    public ResponseEntity<Registro> registrarSaida(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            Registro registroAtualizado = registroService.registrarSaida(idFuncionario);
            return ResponseEntity.ok(registroAtualizado); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found (se não tiver entrada ou já tiver saída)
        }
    }

    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<List<Registro>> listarRegistrosPorFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        List<Registro> registros = registroService.listarRegistrosPorFuncionario(idFuncionario);
        if (registros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(registros);
    }

    @GetMapping
    public ResponseEntity<List<Registro>> listarTodosRegistros() {
        List<Registro> registros = registroService.listarTodosRegistros();
        if (registros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(registros);
    }
}