package com.chidicivok.civokbank.DTOs.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InternalTransferRequest {
    @NotBlank
    private String destinationAccountNumber;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}