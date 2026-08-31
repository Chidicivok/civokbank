package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExchangeRateResponse {

    private Long exchangeRateId;

    private Currency fromCurrency;

    private Currency toCurrency;

    private BigDecimal rate;

    private LocalDateTime updatedAt;
}