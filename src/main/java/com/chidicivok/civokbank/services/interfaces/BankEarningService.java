package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.responses.BankEarningResponse;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.enums.EarningType;

import java.math.BigDecimal;
import java.util.List;

public interface BankEarningService {

    BankEarningResponse recordEarning(String transactionReference, EarningType earningType, BigDecimal amount, Currency currency);

    List<BankEarningResponse> getAllEarnings();

    BigDecimal getTotalEarningsByCurrency(Currency currency);
}