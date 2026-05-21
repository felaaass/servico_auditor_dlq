package com.example.trabalho.application.ports.in;

import com.example.trabalho.core.domain.model.OcorrenciaErroBO;

public interface ProcessarTriagemUseCase {
    void classificarERegistrar(OcorrenciaErroBO ocorrencia);
}