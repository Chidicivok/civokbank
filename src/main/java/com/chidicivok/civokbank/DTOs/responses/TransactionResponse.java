package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.enums.TransactionStatus;
import com.chidicivok.civokbank.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {

    private String transactionReference;

    private TransactionType transactionType;

    private TransactionStatus transactionStatus;

    private BigDecimal amount;

    private BigDecimal fee;

    private Currency sourceCurrency;

    private Currency destinationCurrency;

    private BigDecimal destinationAmount;

    private String sourceAccountNumber;

    private String destinationAccountNumber;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}