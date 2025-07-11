package br.ufrpe.worksmart.worksmart_frequencia_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WorksmartFrequenciaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorksmartFrequenciaServiceApplication.class, args);
	}

	// Bean que define o RestTemplate
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
