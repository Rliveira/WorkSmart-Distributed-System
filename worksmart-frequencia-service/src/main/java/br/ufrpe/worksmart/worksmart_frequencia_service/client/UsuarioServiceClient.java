package br.ufrpe.worksmart.worksmart_frequencia_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate; // Cliente HTTP simples para chamadas REST

// Esta classe representará o Funcionario simplificado para validação
// No futuro, você pode ter uma 'UsuarioDTO' ou 'FuncionarioDTO' compartilhada em um módulo 'common-dtos'
// Por enquanto, uma classe interna simples ou definir os campos diretamente.
// Para simplificar, assumimos que o UsuarioService retorna um JSON que tem um campo 'id'.
// Uma classe DTO simples para receber a resposta do UsuarioService
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
class UsuarioDTO { // Representação simplificada de um usuário recebido do serviço de usuário
    private Long id;
    private String nome;
    private String cpf;
    // Adicione outros campos que você possa precisar do UsuarioService
}


@Component // Marca esta classe como um componente Spring
public class UsuarioServiceClient {

    // Endpoint base do serviço de usuário (configurado no application.properties)
    @Value("${usuarios.service.url}")
    private String usuarioServiceUrl;

    private final RestTemplate restTemplate; // Usaremos RestTemplate para simplicidade

    public UsuarioServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Método para verificar se um funcionário existe pelo ID
    public boolean funcionarioExiste(Long idFuncionario) {
        String url = usuarioServiceUrl + "/api/funcionarios/" + idFuncionario;
        try {
            // Tenta fazer uma requisição GET para o endpoint de buscar funcionário por ID
            ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(url, UsuarioDTO.class);
            return response.getStatusCode() == HttpStatus.OK; // Retorna true se o status for 200 OK
        } catch (Exception e) {
            // Captura qualquer exceção (ex: conexão recusada, 404 Not Found)
            System.err.println("Erro ao verificar existência do funcionário " + idFuncionario + ": " + e.getMessage());
            return false; // Retorna false se houver qualquer erro
        }
    }

    // Método para obter os detalhes de um funcionário pelo ID (se precisar do nome, CPF, etc.)
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