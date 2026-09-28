package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.ExchangeRateUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.ExchangeRateApiResponse;
import com.chidicivok.civokbank.DTOs.responses.ExchangeRateResponse;
import com.chidicivok.civokbank.entities.Admin;
import com.chidicivok.civokbank.entities.AdminAuditLog;
import com.chidicivok.civokbank.entities.ExchangeRate;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.exceptions.ExternalApiFailureException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.ExchangeRateMapper;
import com.chidicivok.civokbank.repositories.AdminAuditLogRepository;
import com.chidicivok.civokbank.repositories.AdminRepository;
import com.chidicivok.civokbank.repositories.ExchangeRateRepository;
import com.chidicivok.civokbank.services.interfaces.ExchangeRateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.time.LocalDateTime;

@Service
public class ExchangeRateServiceImplementation implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final AdminRepository adminRepository;
    private final AdminAuditLogRepository adminAuditLogRepository;
    private final RestClient restClient;

    public ExchangeRateServiceImplementation(ExchangeRateRepository exchangeRateRepository, AdminRepository adminRepository, AdminAuditLogRepository adminAuditLogRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.adminRepository = adminRepository;
        this.adminAuditLogRepository = adminAuditLogRepository;
        this.restClient = RestClient.create("https://api.frankfurter.dev");
    }


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





//    // NOT NEEDED AGAIN - USING FRANKFURTER API
//    @Override
//    @Transactional
//    public ExchangeRateResponse setExchangeRate(String adminEmail, ExchangeRateUpdateRequest request) {
//
//        Admin admin = adminRepository.findByEmail(adminEmail).orElseThrow(
//                () -> new UnAuthorizedPermissionException("You are not permitted to perform this action")
//        );
//
////        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(request.getFromCurrency(), request.getToCurrency())
////                .orElseGet(ExchangeRate::new);
//
//
//        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(request.getFromCurrency(), request.getToCurrency())
//                .orElse(new ExchangeRate());
//
//        exchangeRate.setFromCurrency(request.getFromCurrency());
//        exchangeRate.setToCurrency(request.getToCurrency());
//        exchangeRate.setRate(request.getRate());
//
//        ExchangeRate savedExchangeRate = exchangeRateRepository.save(exchangeRate);
//
//        // create log
//        AdminAuditLog newLog = new AdminAuditLog();
//
//        newLog.setAdmin(admin);
//        newLog.setAction("Update exchange rate");
//        newLog.setTargetAccountNumber(adminEmail);
//        newLog.setReason("To update exchange rate");
//        newLog.setCreatedAt(LocalDateTime.now());
//
//        adminAuditLogRepository.save(newLog);
//
//        return ExchangeRateMapper.toResponse(savedExchangeRate);
//    }
}