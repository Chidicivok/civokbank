package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.enums.CustomerTier;

import java.math.BigDecimal;

public interface BankingFeeService {

    BigDecimal getTransactionFee(CustomerTier customerTier);

    BigDecimal getExternalTransferFee();

    BigDecimal calculateFxCommission(BigDecimal convertedAmount);

    BigDecimal calculateExternalFxCommission(BigDecimal convertedAmount);

}
