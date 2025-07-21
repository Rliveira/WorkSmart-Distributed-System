package br.ufrpe.worksmart.worksmart_usuario_service.services;

import br.ufrpe.worksmart.worksmart_usuario_service.model.Funcionario;
import br.ufrpe.worksmart.worksmart_usuario_service.services.FuncionarioService;
import br.ufrpe.worksmart.worksmart_usuario_service.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository) { // , UsuarioRepository usuarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    // Metodo para salvar (criar ou atualizar) um funcionário
    public Funcionario salvarFuncionario(Funcionario funcionario) {
       return funcionarioRepository.save(funcionario);
    }

    // Metodo para buscar um funcionário por ID
    public Optional<Funcionario> buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    // Metodo para listar todos os funcionários
    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    // Metodo para deletar um funcionário por ID
    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    // Metodo para atualizar dados de um funcionário existente
    public Optional<Funcionario> atualizarFuncionario(Long id, Funcionario funcionarioAtualizado) {
        return funcionarioRepository.findById(id).map(funcionario -> {
            // Atualiza campos de Usuario (herdado)
            funcionario.setNome(funcionarioAtualizado.getNome());
            funcionario.setCpf(funcionarioAtualizado.getCpf());
            funcionario.setLogin(funcionarioAtualizado.getLogin());
            funcionario.setSenha(funcionarioAtualizado.getSenha());

            // Atualiza campos específicos de Funcionario
            funcionario.setTaxaProdutividade(funcionarioAtualizado.getTaxaProdutividade());
            funcionario.setCargo(funcionarioAtualizado.getCargo());
            funcionario.setDataContratacao(funcionarioAtualizado.getDataContratacao());
            funcionario.setSalario(funcionarioAtualizado.getSalario());
            funcionario.setHorarioJornada(funcionarioAtualizado.getHorarioJornada());
            funcionario.setEAdm(funcionarioAtualizado.getEAdm());
            funcionario.setFilial(funcionarioAtualizado.getFilial());
            funcionario.setDispositivoChave(funcionarioAtualizado.getDispositivoChave());

            return funcionarioRepository.save(funcionario);
        });
    }

    // Métodos de negócio específicos de Funcionario
    public Double calcularTempoDeServico(Long id) {
        return funcionarioRepository.findById(id)
                .map(funcionario -> {
                    if (funcionario.getDataContratacao() == null) {
                        return 0.0;
                    }
                    Period period = Period.between(funcionario.getDataContratacao(), LocalDate.now());
                    return (double) period.getYears() + (double) period.getMonths() / 12.0 + (double) period.getDays() / 365.0;
                })
                .orElse(null);
    }

    public Double calcularTaxaProdutividade(Long id) {
        return funcionarioRepository.findById(id)
                .map(Funcionario::getTaxaProdutividade)
                .orElse(null);
    }
}