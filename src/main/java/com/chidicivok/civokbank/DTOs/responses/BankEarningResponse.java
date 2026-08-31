package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.enums.EarningType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BankEarningResponse {

    private Long bankEarningId;

    private String transactionReference;

    private EarningType earningType;

    private BigDecimal amount;

    private Currency currency;

    private LocalDateTime createdAt;
}