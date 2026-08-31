package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.AccountStatus;
import com.chidicivok.civokbank.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountResponse {

    private Long accountId;

    private String accountNumber;

    private AccountStatus accountStatus;

    private BigDecimal balance;

    private Currency currency;

    private Long customerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}