package br.ufrpe.worksmart.worksmart_produtividade_service.client;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.RegistroDTO;
import br.ufrpe.worksmart.worksmart_produtividade_service.exceptions.ServicoIndisponivelException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class FrequenciaServiceClient {

    @Value("${frequencia.service.url}")
    private String frequenciaServiceUrl;

    private final RestTemplate restTemplate;

    public FrequenciaServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RegistroDTO> listarRegistrosPorFuncionario(Long idFuncionario) {
        String url = frequenciaServiceUrl + "/api/registros/funcionario/" + idFuncionario;
        try {
            RegistroDTO[] registrosArray = restTemplate.getForObject(url, RegistroDTO[].class);
            return List.of(registrosArray);
        } catch (Exception e) {
            System.err.println("ERRO DE COMUNICAÇÃO - FREQUENCIA SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Frequencia Service indisponível ou erro ao listar registros para funcionário: " + idFuncionario, e);
        }
    }

    public List<RegistroDTO> listarTodosRegistros() {
        String url = frequenciaServiceUrl + "/api/registros";
        try {
            RegistroDTO[] registrosArray = restTemplate.getForObject(url, RegistroDTO[].class);
            return List.of(registrosArray);
        } catch (Exception e) {
            System.err.println("ERRO DE COMUNICAÇÃO - FREQUENCIA SERVICE: " + e.getMessage());
            throw new ServicoIndisponivelException("Frequencia Service indisponível ou erro ao listar todos os registros.", e);
        }
    }
}