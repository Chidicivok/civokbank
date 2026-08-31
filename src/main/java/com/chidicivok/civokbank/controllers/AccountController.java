package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.requests.AccountCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.DepositRequest;
import com.chidicivok.civokbank.DTOs.requests.WithdrawalRequest;
import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;
import com.chidicivok.civokbank.services.interfaces.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> createAccount(Authentication authentication, @Valid @RequestBody AccountCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(authentication.getName(), request));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(Authentication authentication, @PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(authentication.getName(), accountNumber));
    }

    @GetMapping("/get-all-accounts")
    public ResponseEntity<List<AccountResponse>> getCustomerAccounts(Authentication authentication) {
        return ResponseEntity.ok(accountService.getCustomerAccounts(authentication.getName()));
    }

    @PostMapping("/deposit/{accountNumber}")
    public ResponseEntity<TransactionResponse> deposit(Authentication authentication, @PathVariable String accountNumber, @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(accountService.deposit(authentication.getName(), accountNumber, request));
    }

    @PostMapping("/withdrawal/{accountNumber}")
    public ResponseEntity<TransactionResponse> withdraw(Authentication authentication, @PathVariable String accountNumber, @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(accountService.withdraw(authentication.getName(), accountNumber, request));
    }
}


