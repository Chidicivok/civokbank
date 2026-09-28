package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.responses.ExchangeRateApiResponse;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.services.interfaces.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<ExchangeRateApiResponse> getExchangeRate(@RequestParam Currency fromCurrency, @RequestParam Currency toCurrency) {

        ExchangeRateApiResponse response = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

        return ResponseEntity.ok(response);
    }

//    @PutMapping("set-exhange-rate")
//    public ResponseEntity<ExchangeRateResponse> setExchangeRate(Authentication authentication, @Valid @RequestBody ExchangeRateUpdateRequest request) {
//
//        ExchangeRateResponse response = exchangeRateService.setExchangeRate(authentication.getName(), request);
//
//        return ResponseEntity.ok(response);
//    }
}