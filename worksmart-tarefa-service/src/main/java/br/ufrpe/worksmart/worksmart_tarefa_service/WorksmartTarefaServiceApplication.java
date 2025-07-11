package br.ufrpe.worksmart.worksmart_tarefa_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WorksmartTarefaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorksmartTarefaServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
