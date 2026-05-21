package com.example.trabalho.infrastructure.adapters.in.sqs.converter;

import com.example.trabalho.core.domain.model.ItemPedidoBO;
import com.example.trabalho.core.domain.model.OcorrenciaErroBO;
import com.example.trabalho.infrastructure.adapters.in.sqs.dto.ItemMensagemDTO;
import com.example.trabalho.infrastructure.adapters.in.sqs.dto.MensagemFilaReceptoraDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConversorMensagemParaDominio {

    public OcorrenciaErroBO mapearParaObjetoDominio(MensagemFilaReceptoraDTO dto, String jsonBruto) {
        List<ItemPedidoBO> listaItensBO = new ArrayList<>();

        if (dto.getOrderItems() != null) {
            for (ItemMensagemDTO itemDto : dto.getOrderItems()) {
                listaItensBO.add(new ItemPedidoBO(itemDto.getSku(), itemDto.getAmount()));
            }
        }

        return new OcorrenciaErroBO(
                dto.getZipCode(),
                dto.getCustomerId(),
                listaItensBO,
                dto.getOrigin(),
                dto.getOccurredAt(),
                jsonBruto
        );
    }
}