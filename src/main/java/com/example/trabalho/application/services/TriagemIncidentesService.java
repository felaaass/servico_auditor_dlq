package com.example.trabalho.application.services;

import com.example.trabalho.application.ports.in.ProcessarTriagemUseCase;
import com.example.trabalho.application.ports.out.persistence.SalvarAuditoriaPort;
import com.example.trabalho.core.domain.model.OcorrenciaErroBO;
import org.springframework.stereotype.Service;

@Service
public class TriagemIncidentesService implements ProcessarTriagemUseCase {

    private final SalvarAuditoriaPort salvarAuditoriaPort;

    public TriagemIncidentesService(SalvarAuditoriaPort salvarAuditoriaPort) {
        this.salvarAuditoriaPort = salvarAuditoriaPort;
    }

    @Override
    public void classificarERegistrar(OcorrenciaErroBO ocorrencia) {
        this.salvarAuditoriaPort.persistirDadosInspecao(ocorrencia);
    }
}