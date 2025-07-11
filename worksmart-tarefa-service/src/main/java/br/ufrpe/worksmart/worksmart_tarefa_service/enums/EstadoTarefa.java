
package br.ufrpe.worksmart.worksmart_tarefa_service.enums;

public enum EstadoTarefa {
    EM_PROGRESSO("Em Progresso"),
    CONCLUIDA("Concluída"),
    NAO_REALIZADA("Não Realizada");

    private final String descricao;

    EstadoTarefa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}