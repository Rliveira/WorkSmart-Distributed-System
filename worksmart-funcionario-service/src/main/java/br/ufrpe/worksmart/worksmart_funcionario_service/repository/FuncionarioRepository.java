package br.ufrpe.worksmart.worksmart_funcionario_service.repository;

import br.ufrpe.worksmart.worksmart_funcionario_service.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>
{

}