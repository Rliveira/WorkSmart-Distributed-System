package br.ufrpe.worksmart.worksmart_frequencia_service.services;

import br.ufrpe.worksmart.worksmart_frequencia_service.client.UsuarioServiceClient;
import br.ufrpe.worksmart.worksmart_frequencia_service.model.Registro;
import br.ufrpe.worksmart.worksmart_frequencia_service.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final UsuarioServiceClient usuarioServiceClient; // Cliente para o serviço de usuário

    @Autowired
    public RegistroService(RegistroRepository registroRepository, UsuarioServiceClient usuarioServiceClient) {
        this.registroRepository = registroRepository;
        this.usuarioServiceClient = usuarioServiceClient;
    }

    // Métodos de negócio para Registro de Ponto

    public Registro registrarEntrada(Long idFuncionario) {
        // 1. Validar se o funcionário existe usando o serviço de usuário
        if (!usuarioServiceClient.funcionarioExiste(idFuncionario)) {
            throw new IllegalArgumentException("Funcionário com ID " + idFuncionario + " não encontrado.");
        }

        // 2. Verificar se já existe uma entrada para o funcionário no dia atual sem saída
        LocalDate hoje = LocalDate.now();
        if (registroRepository.findByIdFuncionarioAndDataRegistroAndHoraSaidaIsNull(idFuncionario, hoje).isPresent()) {
            throw new IllegalStateException("Funcionário com ID " + idFuncionario + " já registrou entrada hoje.");
        }

        // 3. Criar e salvar o novo registro de entrada
        Registro novoRegistro = new Registro();
        novoRegistro.setIdFuncionario(idFuncionario);
        novoRegistro.setDataRegistro(hoje);
        novoRegistro.setHoraEntrada(LocalTime.now());
        // horaSaida permanece nula inicialmente
        return registroRepository.save(novoRegistro);
    }

    public Registro registrarSaida(Long idFuncionario) {
        // 1. Validar se o funcionário existe
        if (!usuarioServiceClient.funcionarioExiste(idFuncionario)) {
            throw new IllegalArgumentException("Funcionário com ID " + idFuncionario + " não encontrado.");
        }

        // 2. Encontrar o registro de entrada mais recente para o funcionário no dia atual sem hora de saída
        LocalDate hoje = LocalDate.now();
        Optional<Registro> registroOpt = registroRepository.findByIdFuncionarioAndDataRegistroAndHoraSaidaIsNull(idFuncionario, hoje);

        if (registroOpt.isEmpty()) {
            throw new IllegalStateException("Funcionário com ID " + idFuncionario + " não possui entrada registrada ou já registrou saída hoje.");
        }

        // 3. Atualizar o registro com a hora de saída
        Registro registro = registroOpt.get();
        registro.setHoraSaida(LocalTime.now());
        return registroRepository.save(registro);
    }

    public List<Registro> listarRegistrosPorFuncionario(Long idFuncionario) {
        return registroRepository.findByIdFuncionario(idFuncionario);
    }

    public List<Registro> listarTodosRegistros() {
        return registroRepository.findAll();
    }

    // Você pode adicionar mais métodos aqui conforme a necessidade (ex: buscar registros por data, etc.)
}