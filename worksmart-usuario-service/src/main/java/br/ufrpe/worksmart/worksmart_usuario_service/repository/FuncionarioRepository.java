package br.ufrpe.worksmart.worksmart_usuario_service.repository;

import br.ufrpe.worksmart.worksmart_usuario_service.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>
{

}