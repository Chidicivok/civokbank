package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.enums.CustomerTier;
import com.chidicivok.civokbank.services.interfaces.BankingFeeService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankingFeeServiceImplementation implements BankingFeeService {

    // constant bank charges for each tier and fx exchange
    private static final BigDecimal STANDARD_FEE = new BigDecimal("25.00");
    private static final BigDecimal PREMIUM_FEE = new BigDecimal("50.00");
    private static final BigDecimal EXTERNAL_TRANSFER_FEE = new BigDecimal("100.00");
    private static final BigDecimal FX_COMMISSION_RATE = new BigDecimal("0.01");
    private static final BigDecimal EXTERNAL_FX_COMMISSION_RATE = new BigDecimal("0.03");

    @Override
    public BigDecimal getTransactionFee(CustomerTier customerTier) {
        if (customerTier == CustomerTier.PREMIUM) {return PREMIUM_FEE;}
        return STANDARD_FEE;
    }

    @Override
    public BigDecimal getExternalTransferFee() {
        return EXTERNAL_TRANSFER_FEE;
    }

    @Override
    public BigDecimal calculateFxCommission(BigDecimal convertedAmount) {
        return convertedAmount.multiply(FX_COMMISSION_RATE);
    }

    @Override
    public BigDecimal calculateExternalFxCommission(BigDecimal convertedAmount) {
        return convertedAmount.multiply(EXTERNAL_FX_COMMISSION_RATE);
    }


}