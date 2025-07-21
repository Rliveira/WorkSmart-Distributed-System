package br.ufrpe.worksmart.worksmart_produtividade_service.client;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.FuncionarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Component
public class UsuarioServiceClient {

    @Value("${usuarios.service.url}")
    private String usuarioServiceUrl;

    private final RestTemplate restTemplate;

    public UsuarioServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<FuncionarioDTO> getFuncionarioDetails(Long idFuncionario) {
        String url = usuarioServiceUrl + "/api/funcionarios/" + idFuncionario;
        try {
            ResponseEntity<FuncionarioDTO> response = restTemplate.getForEntity(url, FuncionarioDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter detalhes do funcionário " + idFuncionario + ": " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<FuncionarioDTO> listarTodosFuncionarios() {
        String url = usuarioServiceUrl + "/api/funcionarios";
        try {
            // RestTemplate para listas precisa de um array para desserialização robusta
            FuncionarioDTO[] funcionariosArray = restTemplate.getForObject(url, FuncionarioDTO[].class);
            return List.of(funcionariosArray); // Converte array para List (Java 9+)
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os funcionários: " + e.getMessage());
        }
        return List.of(); // Retorna lista vazia em caso de erro
    }
}