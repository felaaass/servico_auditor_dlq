package com.example.trabalho.infrastructure.adapters.in.sqs.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.trabalho.application.ports.in.ProcessarTriagemUseCase;
import com.example.trabalho.core.domain.model.OcorrenciaErroBO;
import com.example.trabalho.infrastructure.adapters.in.sqs.dto.MensagemFilaReceptoraDTO;
import com.example.trabalho.infrastructure.adapters.in.sqs.converter.ConversorMensagemParaDominio;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OuvinteFilaDlqAdapter {

    private static final Logger logger = LoggerFactory.getLogger(OuvinteFilaDlqAdapter.class);
    private final ProcessarTriagemUseCase triagemUseCase;
    private final ObjectMapper jsonMapper;
    private final ConversorMensagemParaDominio conversorDominio;

    // ALTERADO AQUI: O ObjectMapper voltou para o parâmetro do construtor
    public OuvinteFilaDlqAdapter(ProcessarTriagemUseCase triagemUseCase,
                                 ConversorMensagemParaDominio conversorDominio,
                                 ObjectMapper jsonMapper) {
        this.triagemUseCase = triagemUseCase;
        this.conversorDominio = conversorDominio;
        this.jsonMapper = jsonMapper; // O Spring agora entrega o Bean pronto aqui
    }

    @SqsListener("${queue.order-events}")
    public void processarEntradaDlq(String mensagemJson) {
        try {
            logger.info("Mensagem recebida da DLQ: {}", mensagemJson);
            MensagemFilaReceptoraDTO dadosBrutos = jsonMapper.readValue(mensagemJson, MensagemFilaReceptoraDTO.class);
            OcorrenciaErroBO negocioBO = conversorDominio.mapearParaObjetoDominio(dadosBrutos, mensagemJson);
            triagemUseCase.classificarERegistrar(negocioBO);
            logger.info("Mensagem persistida com sucesso.");
        } catch (Exception ex) {
            logger.error("Erro no processamento. Mensagem retida na DLQ.", ex);
            throw new RuntimeException("Falha ao processar DLQ", ex);
        }
    }
}