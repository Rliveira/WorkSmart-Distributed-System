package br.ufrpe.worksmart.worksmart_tarefa_service.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

// DTO simplificado para a resposta do UsuarioService
@Data
@NoArgsConstructor
@AllArgsConstructor
class UsuarioDTO {
    private Long id;
    private String nome;
    private String cpf;
}

@Component
public class UsuarioServiceClient {

    @Value("${usuarios.service.url}")
    private String usuarioServiceUrl;

    private final RestTemplate restTemplate;

    public UsuarioServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean funcionarioExiste(Long idFuncionario) {
        String url = usuarioServiceUrl + "/api/funcionarios/" + idFuncionario;
        try {
            ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(url, UsuarioDTO.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Erro ao verificar existência do funcionário " + idFuncionario + ": " + e.getMessage());
            return false;
        }
    }

    public Optional<UsuarioDTO> getFuncionarioDetails(Long idFuncionario) {
        String url = usuarioServiceUrl + "/api/funcionarios/" + idFuncionario;
        try {
            ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(url, UsuarioDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter detalhes do funcionário " + idFuncionario + ": " + e.getMessage());
        }
        return Optional.empty();
    }
}