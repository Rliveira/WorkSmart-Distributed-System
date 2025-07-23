package br.ufrpe.worksmart.worksmart_produtividade_service.exceptions;

public class ServicoIndisponivelException extends RuntimeException {
    public ServicoIndisponivelException(String message) {
        super(message);
    }

    public ServicoIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
}