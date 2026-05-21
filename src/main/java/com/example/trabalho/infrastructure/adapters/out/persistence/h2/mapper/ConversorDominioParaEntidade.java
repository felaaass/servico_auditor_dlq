package com.example.trabalho.infrastructure.adapters.out.persistence.h2.mapper;

import com.example.trabalho.core.domain.model.OcorrenciaErroBO;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.entity.RegistroAuditoriaEntity;
import org.springframework.stereotype.Component;

@Component
public class ConversorDominioParaEntidade {

    public RegistroAuditoriaEntity converterParaLinhaTabela(OcorrenciaErroBO bo) {
        RegistroAuditoriaEntity entidade = new RegistroAuditoriaEntity();
        entidade.setPayload(bo.getConteudoOriginalJson());
        return entidade;
    }
}