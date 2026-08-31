package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.BankEarningResponse;
import com.chidicivok.civokbank.entities.BankEarning;

public class BankEarningMapper {

    public static BankEarningResponse toResponse(BankEarning bankEarning) {

        BankEarningResponse response = new BankEarningResponse();

        response.setBankEarningId(bankEarning.getBankEarningId());
        response.setTransactionReference(bankEarning.getTransactionReference());
        response.setEarningType(bankEarning.getEarningType());
        response.setAmount(bankEarning.getAmount());
        response.setCurrency(bankEarning.getCurrency());
        response.setCreatedAt(bankEarning.getCreatedAt());

        return response;
    }
}