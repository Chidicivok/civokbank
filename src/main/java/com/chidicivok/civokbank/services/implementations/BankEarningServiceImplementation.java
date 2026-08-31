package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.responses.BankEarningResponse;
import com.chidicivok.civokbank.entities.BankEarning;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.enums.EarningType;
import com.chidicivok.civokbank.mappers.BankEarningMapper;
import com.chidicivok.civokbank.repositories.BankEarningRepository;
import com.chidicivok.civokbank.services.interfaces.BankEarningService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankEarningServiceImplementation implements BankEarningService {

    private final BankEarningRepository bankEarningRepository;


    public BankEarningServiceImplementation(BankEarningRepository bankEarningRepository) {
        this.bankEarningRepository = bankEarningRepository;
    }

    @Override
    public BankEarningResponse recordEarning(String transactionReference, EarningType earningType, BigDecimal amount, Currency currency) {

        BankEarning bankEarning = new BankEarning();

        bankEarning.setTransactionReference(transactionReference);
        bankEarning.setEarningType(earningType);
        bankEarning.setAmount(amount);
        bankEarning.setCurrency(currency);

        BankEarning savedEarning = bankEarningRepository.save(bankEarning);

        return BankEarningMapper.toResponse(savedEarning);
    }


    // enable for admins
    @Override
    public List<BankEarningResponse> getAllEarnings() {

        return bankEarningRepository.findAll()
                .stream()
                .map(BankEarningMapper::toResponse)
                .toList();
    }

    // enable for admins
    @Override
    public BigDecimal getTotalEarningsByCurrency(Currency currency) {

        return bankEarningRepository.findByCurrency(currency)
                .stream()
                .map(BankEarning::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}