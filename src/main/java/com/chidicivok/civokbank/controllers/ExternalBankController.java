package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.responses.ExternalBankAccountResponse;
import com.chidicivok.civokbank.DTOs.responses.ExternalBankResponse;
import com.chidicivok.civokbank.services.interfaces.ExternalBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external-banks")
public class ExternalBankController {

    private final ExternalBankService externalBankService;

    public ExternalBankController(ExternalBankService externalBankService) {
        this.externalBankService = externalBankService;
    }


    @GetMapping
    public ResponseEntity<List<ExternalBankResponse>> getAllExternalBanks() {

        List<ExternalBankResponse> banks = externalBankService.getAllExternalBanks();
        return ResponseEntity.ok(banks);
    }


    @GetMapping("/{bankCode}")
    public ResponseEntity<ExternalBankResponse> getBankByCode(@PathVariable String bankCode) {

        ExternalBankResponse bank = externalBankService.getBankByCode(bankCode);
        return ResponseEntity.ok(bank);
    }


    @GetMapping("/{bankCode}/accounts/{accountNumber}")
    public ResponseEntity<ExternalBankAccountResponse> getExternalAccount(@PathVariable String bankCode, @PathVariable String accountNumber) {
        ExternalBankAccountResponse account = externalBankService.getExternalAccount(bankCode, accountNumber);
        return ResponseEntity.ok(account);
    }
}