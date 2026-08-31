package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.VaultStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VaultResponse {

    private Long vaultId;

    private BigDecimal principalAmount;

    private BigDecimal interestRate;

    private BigDecimal expectedInterest;

    private LocalDateTime lockedAt;

    private LocalDateTime maturityAt;

    private Long accountId;

    private VaultStatus vaultStatus;
}