// worksmart-produtividade-service/src/main/java/br/ufrpe/worksmart.produtividade/client/TarefaServiceClient.java
package br.ufrpe.worksmart.worksmart_produtividade_service.client;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.ConstrucaoDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.ProjetoDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.TarefaDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.ViagemDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.exceptions.ServicoIndisponivelException;
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConstrucaoDTO.class, name = "Construcao"),
        @JsonSubTypes.Type(value = ViagemDTO.class, name = "Viagem")
})

@Component
public class TarefaServiceClient {

    @Value("${tarefas.service.url}")
    private String tarefaServiceUrl;

    private final RestTemplate restTemplate;

    public TarefaServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TarefaDTO> listarTodasTarefas() {
        String url = tarefaServiceUrl + "/api/tarefas";
        try {
            ResponseEntity<List<TarefaDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TarefaDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : List.of();
        } catch (Exception e) {
            System.err.println("ERRO DE COMUNICAÇÃO - TAREFA SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Tarefa Service indisponível ou erro ao listar todas as tarefas.", e);
        }
    }

    public List<TarefaDTO> listarTarefasPorFuncionario(Long idFuncionario) {
        String url = tarefaServiceUrl + "/api/tarefas/funcionario/" + idFuncionario;
        try {
            ResponseEntity<List<TarefaDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TarefaDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : List.of();
        } catch (Exception e) {
            System.err.println("ERRO DE COMUNICAÇÃO - TAREFA SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Tarefa Service indisponível ou erro ao listar tarefas do funcionário: " + idFuncionario, e);
        }
    }

    public Optional<TarefaDTO> buscarTarefaPorId(Long idTarefa) {
        String url = tarefaServiceUrl + "/api/tarefas/" + idTarefa;
        try {
            ResponseEntity<TarefaDTO> response = restTemplate.getForEntity(url, TarefaDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
            return Optional.empty(); // Se 404, retorna Optional.empty
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return Optional.empty(); // Especificamente para 404 Not Found
        } catch (Exception e) {
            System.err.println("ERRO DE COMUNICAÇÃO - TAREFA SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Tarefa Service indisponível ou erro ao buscar tarefa: " + idTarefa, e);
        }
    }

    // Metodo para buscar um Projeto por ID (Este metodo assumiria um futuro worksmart-projeto-service)
    // Se o futuro worksmart-projeto-service gerenciar Projetos e suas listas de Tarefas
    public Optional<ProjetoDTO> buscarProjetoPorId(Long idProjeto) {
        // Exemplo de URL para um futuro worksmart-projeto-service
        // String url = "http://localhost:8084/api/projetos/" + idProjeto;
        // try {
        //     ResponseEntity<ProjetoDTO> response = restTemplate.getForEntity(url, ProjetoDTO.class);
        //     if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        //         return Optional.of(response.getBody());
        //     }
        // } catch (Exception e) {
        //     System.err.println("Erro ao buscar projeto " + idProjeto + ": " + e.getMessage());
        //     e.printStackTrace();
        // }
        System.err.println("Método buscarProjetoPorId não implementado para um serviço de Projetos real neste cliente.");
        return Optional.empty();
    }
}