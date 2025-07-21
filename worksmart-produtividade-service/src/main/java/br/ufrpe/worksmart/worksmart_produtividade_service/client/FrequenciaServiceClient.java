package br.ufrpe.worksmart.worksmart_produtividade_service.client;

import br.ufrpe.worksmart.worksmart_produtividade_service.DTOs.RegistroDTO;
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
            System.err.println("Erro ao listar registros do funcionário " + idFuncionario + ": " + e.getMessage());
        }
        return List.of();
    }

    public List<RegistroDTO> listarTodosRegistros() {
        String url = frequenciaServiceUrl + "/api/registros";
        try {
            RegistroDTO[] registrosArray = restTemplate.getForObject(url, RegistroDTO[].class);
            return List.of(registrosArray);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os registros: " + e.getMessage());
        }
        return List.of();
    }
}