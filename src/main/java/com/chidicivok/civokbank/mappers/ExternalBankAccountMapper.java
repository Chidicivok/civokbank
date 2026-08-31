package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.ExternalBankAccountResponse;
import com.chidicivok.civokbank.entities.ExternalBankAccount;

public class ExternalBankAccountMapper {

    public static ExternalBankAccountResponse toResponse(ExternalBankAccount externalBankAccount) {

        ExternalBankAccountResponse response = new ExternalBankAccountResponse();

        response.setExternalAccountId(externalBankAccount.getExternalAccountId());
        response.setAccountNumber(externalBankAccount.getAccountNumber());
        response.setAccountName(externalBankAccount.getAccountName());
        response.setCurrency(externalBankAccount.getCurrency());
        response.setBalance(externalBankAccount.getBalance());
        response.setBankName(externalBankAccount.getExternalBank().getBankName());
        response.setBankCode(externalBankAccount.getExternalBank().getBankCode());

        return response;
    }
}