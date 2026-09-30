package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.responses.ExchangeRateApiResponse;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.exceptions.ExternalApiFailureException;
import com.chidicivok.civokbank.services.interfaces.ExchangeRateService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ExchangeRateServiceImplementation implements ExchangeRateService {


    private final RestClient restClient;

    public ExchangeRateServiceImplementation() {
        this.restClient = RestClient.create("https://api.frankfurter.dev");
    }


    // get exchange rate info from frankfurter api based on from currency == base and to currency == quote
    @Override
    public ExchangeRateApiResponse getExchangeRate(Currency fromCurrency, Currency toCurrency) {

        try {

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/rate/{base}/{quote}")
                            .build(
                                    fromCurrency.name(),
                                    toCurrency.name()
                            )
                    )
                    .retrieve()
                    .body(ExchangeRateApiResponse.class);

        } catch (RestClientException e) {
            throw new ExternalApiFailureException("Unable to retrieve exchange rate information at the moment");
        }
    }




}