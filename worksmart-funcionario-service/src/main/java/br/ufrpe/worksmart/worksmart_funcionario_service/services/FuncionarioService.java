package br.ufrpe.worksmart.worksmart_funcionario_service.services;

import br.ufrpe.worksmart.worksmart_funcionario_service.model.Funcionario;
import br.ufrpe.worksmart.worksmart_funcionario_service.repository.FuncionarioRepository;
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
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) {
        // Lógica de negócio antes de salvar, se houver
        // Ex: calcular tempo de serviço inicial, validar dados mais complexos
        return funcionarioRepository.save(funcionario);
    }

    public Optional<Funcionario> buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public Optional<Funcionario> atualizarFuncionario(Long id, Funcionario funcionarioAtualizado) {
        return funcionarioRepository.findById(id).map(funcionario -> {
            // Atualiza apenas os campos que podem ser editados
            funcionario.setNome(funcionarioAtualizado.getNome());
            funcionario.setCargo(funcionarioAtualizado.getCargo());
            funcionario.setCpf(funcionarioAtualizado.getCpf());
            funcionario.setLogin(funcionarioAtualizado.getLogin());
            funcionario.setSenha(funcionarioAtualizado.getSenha());
            funcionario.setTaxaProdutividade(funcionarioAtualizado.getTaxaProdutividade());
            funcionario.setDataContratacao(funcionarioAtualizado.getDataContratacao());
            funcionario.setSalario(funcionarioAtualizado.getSalario());
            funcionario.setHorarioJornada(funcionarioAtualizado.getHorarioJornada());
            funcionario.setEAdm(funcionarioAtualizado.getEAdm());
            funcionario.setFilial(funcionarioAtualizado.getFilial());
            funcionario.setDispositivoChave(funcionarioAtualizado.getDispositivoChave());

            return funcionarioRepository.save(funcionario);
        });
    }

    // Método para calcularTempoDeServico (do diagrama)
    public Double calcularTempoDeServico(Long id) {
        return funcionarioRepository.findById(id)
                .map(funcionario -> {
                    if (funcionario.getDataContratacao() == null) {
                        return 0.0;
                    }
                    Period period = Period.between(funcionario.getDataContratacao(), LocalDate.now());
                    // Retorna o tempo de serviço em anos (pode ser ajustado para meses, dias, etc.)
                    return (double) period.getYears() + (double) period.getMonths() / 12.0 + (double) period.getDays() / 365.0;
                })
                .orElse(null); // Retorna null se funcionário não encontrado
    }

    // Método para calcularTaxaProdutividade (do diagrama)
    public Double calcularTaxaProdutividade(Long id) {
        // Esta é uma lógica de negócio complexa que provavelmente envolverá outros serviços (Tarefas, Frequência).
        // Por enquanto, vamos retornar a taxa de produtividade que está salva no próprio objeto Funcionario.
        // No futuro, este método faria chamadas a outros microserviços.
        return funcionarioRepository.findById(id)
                .map(Funcionario::getTaxaProdutividade)
                .orElse(null); // Retorna null se funcionário não encontrado
    }
}