package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.ExchangeRateUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.ExchangeRateResponse;
import com.chidicivok.civokbank.entities.Admin;
import com.chidicivok.civokbank.entities.AdminAuditLog;
import com.chidicivok.civokbank.entities.ExchangeRate;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.ExchangeRateMapper;
import com.chidicivok.civokbank.repositories.AdminAuditLogRepository;
import com.chidicivok.civokbank.repositories.AdminRepository;
import com.chidicivok.civokbank.repositories.ExchangeRateRepository;
import com.chidicivok.civokbank.services.interfaces.ExchangeRateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ExchangeRateServiceImplementation implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final AdminRepository adminRepository;
    private final AdminAuditLogRepository adminAuditLogRepository;

    public ExchangeRateServiceImplementation(ExchangeRateRepository exchangeRateRepository, AdminRepository adminRepository, AdminAuditLogRepository adminAuditLogRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.adminRepository = adminRepository;
        this.adminAuditLogRepository = adminAuditLogRepository;
    }


    @Override
    public ExchangeRateResponse getExchangeRate(Currency fromCurrency, Currency toCurrency) {

        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency).orElseThrow(
                () -> new ResourceNotFoundException("Exchange rate not found for " + fromCurrency + " to " + toCurrency)
        );

        return ExchangeRateMapper.toResponse(exchangeRate);
    }


    @Override
    @Transactional
    public ExchangeRateResponse setExchangeRate(String adminEmail,  ExchangeRateUpdateRequest request) {

        Admin admin = adminRepository.findByEmail(adminEmail).orElseThrow(
                () -> new UnAuthorizedPermissionException("You are not permitted to perform this action")
        );

        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(request.getFromCurrency(), request.getToCurrency())
                .orElseGet(ExchangeRate::new);

        exchangeRate.setFromCurrency(request.getFromCurrency());
        exchangeRate.setToCurrency(request.getToCurrency());
        exchangeRate.setRate(request.getRate());

        ExchangeRate savedExchangeRate = exchangeRateRepository.save(exchangeRate);

        // create log
        AdminAuditLog newLog = new AdminAuditLog();

        newLog.setAdmin(admin                       );
        newLog.setAction("Update exchange rate");
        newLog.setTargetAccountNumber(adminEmail);
        newLog.setReason("To update exchange rate");
        newLog.setCreatedAt(LocalDateTime.now());

        adminAuditLogRepository.save(newLog);

        return ExchangeRateMapper.toResponse(savedExchangeRate);
    }
}