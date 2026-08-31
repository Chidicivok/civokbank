package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.ExternalBankResponse;
import com.chidicivok.civokbank.entities.ExternalBank;

public class ExternalBankMapper {

    public static ExternalBankResponse toResponse(ExternalBank externalBank) {

        ExternalBankResponse response = new ExternalBankResponse();

        response.setExternalBankId(externalBank.getExternalBankId());
        response.setBankName(externalBank.getBankName());
        response.setBankCode(externalBank.getBankCode());

        return response;
    }
}