package com.chidicivok.civokbank.DTOs.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawalRequest {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}