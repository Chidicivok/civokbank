package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.responses.ExternalBankAccountResponse;
import com.chidicivok.civokbank.DTOs.responses.ExternalBankResponse;
import com.chidicivok.civokbank.entities.ExternalBank;
import com.chidicivok.civokbank.entities.ExternalBankAccount;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.mappers.ExternalBankAccountMapper;
import com.chidicivok.civokbank.mappers.ExternalBankMapper;
import com.chidicivok.civokbank.repositories.ExternalBankAccountRepository;
import com.chidicivok.civokbank.repositories.ExternalBankRepository;
import com.chidicivok.civokbank.services.interfaces.ExternalBankService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalBankServiceImplementation implements ExternalBankService {

    private final ExternalBankRepository externalBankRepository;
    private final ExternalBankAccountRepository externalBankAccountRepository;

    public ExternalBankServiceImplementation(ExternalBankRepository externalBankRepository, ExternalBankAccountRepository externalBankAccountRepository) {
        this.externalBankRepository = externalBankRepository;
        this.externalBankAccountRepository = externalBankAccountRepository;
    }


    @Override
    public List<ExternalBankResponse> getAllExternalBanks() {

        return externalBankRepository                .findAll()
                .stream()
                .map(ExternalBankMapper::toResponse)
                .toList();
    }


    @Override
    public ExternalBankResponse getBankByCode(String bankCode) {
        ExternalBank bank = externalBankRepository.findByBankCode(bankCode).orElseThrow(
                () -> new ResourceNotFoundException("External bank not found")
        );

        return ExternalBankMapper.toResponse(bank);
    }


    @Override
    public ExternalBankAccountResponse getExternalAccount(String bankCode, String accountNumber) {

        ExternalBankAccount account = externalBankAccountRepository.findByExternalBankBankCodeAndAccountNumber(bankCode, accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("External bank account not found")
        );

        return ExternalBankAccountMapper.toResponse(account);
    }
}