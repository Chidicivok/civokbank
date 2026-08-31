package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.ExchangeRateResponse;
import com.chidicivok.civokbank.entities.ExchangeRate;

public class ExchangeRateMapper {

    public static ExchangeRateResponse toResponse(ExchangeRate exchangeRate) {

        ExchangeRateResponse response = new ExchangeRateResponse();

        response.setExchangeRateId(exchangeRate.getExchangeRateId());
        response.setFromCurrency(exchangeRate.getFromCurrency());
        response.setToCurrency(exchangeRate.getToCurrency());
        response.setRate(exchangeRate.getRate());
        response.setUpdatedAt(exchangeRate.getUpdatedAt());

        return response;
    }
}