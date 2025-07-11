// worksmart-tarefa-service/src/main/java/br/ufrpe/worksmart/tarefa/repository/TarefaRepository.java
package br.ufrpe.worksmart.worksmart_tarefa_service.repository;

import br.ufrpe.worksmart.worksmart_tarefa_service.model.Tarefa;
import br.ufrpe.worksmart.worksmart_tarefa_service.enums.EstadoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // Métodos personalizados úteis:

    // Listar tarefas atribuídas a um funcionário (como atribuidor ou executor)
    List<Tarefa> findByIdAtribuidorOrIdsExecutoresContaining(Long idAtribuidor, Long idExecutor);

    // Listar tarefas por estado (Em Progresso, Concluída, Não Realizada)
    List<Tarefa> findByEstadoTarefa(EstadoTarefa estado);

    // Listar tarefas com prazo estourado que ainda não foram marcadas como "Não Realizada"
    List<Tarefa> findByDataParaConclusaoBeforeAndEstadoTarefa(LocalDate data, EstadoTarefa estado);
}