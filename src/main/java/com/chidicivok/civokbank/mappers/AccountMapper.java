package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.entities.Account;

public class AccountMapper {

    public static AccountResponse toResponse(Account account) {

        AccountResponse response = new AccountResponse();

        response.setAccountId(account.getAccountId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountStatus(account.getAccountStatus());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setCustomerId(account.getCustomer().getCustomerId());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());

        return response;
    }
}