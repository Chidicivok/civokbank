package com.chidicivok.civokbank.DTOs.requests;

import com.chidicivok.civokbank.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRateUpdateRequest {
    @NotNull
    private Currency fromCurrency;

    @NotNull
    private Currency toCurrency;

    @NotNull
    @DecimalMin(value = "0.000001")
    private BigDecimal rate;
}