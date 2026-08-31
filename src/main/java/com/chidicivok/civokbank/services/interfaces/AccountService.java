package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.AccountCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.DepositRequest;
import com.chidicivok.civokbank.DTOs.requests.WithdrawalRequest;
import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(String customerEmail, AccountCreateRequest request);

    AccountResponse getAccountByNumber(String customerEmail, String accountNumber);

    List<AccountResponse> getCustomerAccounts(String customerEmail);

    TransactionResponse deposit(String customerEmail, String accountNumber, DepositRequest request);

    TransactionResponse withdraw(String customerEmail, String accountNumber, WithdrawalRequest request);
}