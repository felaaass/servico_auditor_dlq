package com.example.trabalho.application.ports.out.persistence;

import com.example.trabalho.core.domain.model.OcorrenciaErroBO;

public interface SalvarAuditoriaPort {
    void persistirDadosInspecao(OcorrenciaErroBO ocorrencia);
}