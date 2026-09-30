package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.responses.ExchangeRateApiResponse;
import com.chidicivok.civokbank.enums.Currency;

public interface ExchangeRateService {

    ExchangeRateApiResponse getExchangeRate(Currency fromCurrency, Currency toCurrency);


}