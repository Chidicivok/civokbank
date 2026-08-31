package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.responses.ExternalBankAccountResponse;
import com.chidicivok.civokbank.DTOs.responses.ExternalBankResponse;

import java.util.List;

public interface ExternalBankService {

    List<ExternalBankResponse> getAllExternalBanks();

    ExternalBankResponse getBankByCode(String bankCode);

    ExternalBankAccountResponse getExternalAccount(String bankCode, String accountNumber);
}