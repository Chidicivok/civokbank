package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExternalBankAccountResponse {

    private Long externalAccountId;

    private String accountNumber;

    private String accountName;

    private Currency currency;

    private BigDecimal balance;

    private String bankName;

    private String bankCode;
}