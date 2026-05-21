package com.example.trabalho.core.domain.exceptions;

public class ViolacaoRegraNegocioException extends RuntimeException {
    public ViolacaoRegraNegocioException(String mensagem) {
        super(mensagem);
    }
}