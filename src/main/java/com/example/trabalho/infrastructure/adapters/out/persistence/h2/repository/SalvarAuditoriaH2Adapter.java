package com.example.trabalho.infrastructure.adapters.out.persistence.h2.repository;

import com.example.trabalho.application.ports.out.persistence.SalvarAuditoriaPort;
import com.example.trabalho.core.domain.model.OcorrenciaErroBO;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.entity.RegistroAuditoriaEntity;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.mapper.ConversorDominioParaEntidade;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.jpa.AuditoriaSpringDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class SalvarAuditoriaH2Adapter implements SalvarAuditoriaPort {

    private final AuditoriaSpringDataRepository dbRepository;
    private final ConversorDominioParaEntidade conversorEntidade;
    private final String nomeFilaConfigurada;

    public SalvarAuditoriaH2Adapter(AuditoriaSpringDataRepository dbRepository,
                                    ConversorDominioParaEntidade conversorEntidade,
                                    @Value("${queue.order-events}") String nomeFilaConfigurada) {
        this.dbRepository = dbRepository;
        this.conversorEntidade = conversorEntidade;
        this.nomeFilaConfigurada = nomeFilaConfigurada;
    }

    @Override
    public void persistirDadosInspecao(OcorrenciaErroBO ocorrencia) {
        RegistroAuditoriaEntity tabelaLinha = conversorEntidade.converterParaLinhaTabela(ocorrencia);

        tabelaLinha.setErrorId(UUID.randomUUID().toString());
        tabelaLinha.setQueueName(this.nomeFilaConfigurada);
        tabelaLinha.setTimestamp(OffsetDateTime.now().toString());
        tabelaLinha.setStatus("PENDING_ANALYSIS");
        tabelaLinha.setSeverity(ocorrencia.classificarNivelSeveridade());

        dbRepository.save(tabelaLinha);
    }
}