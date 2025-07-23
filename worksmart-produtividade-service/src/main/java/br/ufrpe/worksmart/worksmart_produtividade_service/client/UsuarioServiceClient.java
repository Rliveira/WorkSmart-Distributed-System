package br.ufrpe.worksmart.worksmart_produtividade_service.client;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.FuncionarioDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.exceptions.ServicoIndisponivelException;
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

    public Optional<FuncionarioDTO> getFuncionarioDetails(Long idFuncionario) throws ServicoIndisponivelException {
        String url = usuarioServiceUrl + "/api/funcionarios/" + idFuncionario;
        try {
            ResponseEntity<FuncionarioDTO> response = restTemplate.getForEntity(url, FuncionarioDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
            // Se o status não for OK, tratamos como não encontrado sem lançar exceção de conexão
            return Optional.empty();
        } catch (Exception e) {
            // Captura qualquer exceção de comunicação e relança uma exceção de serviço indisponível
            System.err.println("ERRO DE COMUNICAÇÃO - USUARIO SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Usuario Service indisponível ou erro ao buscar funcionário: " + idFuncionario, e);
        }
    }

    public List<FuncionarioDTO> listarTodosFuncionarios() throws ServicoIndisponivelException {
        String url = usuarioServiceUrl + "/api/funcionarios";
        try {
            FuncionarioDTO[] funcionariosArray = restTemplate.getForObject(url, FuncionarioDTO[].class);
            return List.of(funcionariosArray);
        } catch (Exception e) {
            System.err.println("ERRO DE COMUNICAÇÃO - USUARIO SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Usuario Service indisponível ou erro ao listar funcionários.", e);
        }
    }
}