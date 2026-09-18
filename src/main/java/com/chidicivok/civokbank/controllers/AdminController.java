package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.requests.AccountStatusUpdateRequest;
import com.chidicivok.civokbank.DTOs.requests.AdminCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerTierUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.DTOs.responses.AdminResponse;
import com.chidicivok.civokbank.DTOs.responses.BankEarningResponse;
import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.services.interfaces.AdminAuditLogService;
import com.chidicivok.civokbank.services.interfaces.AdminService;
import com.chidicivok.civokbank.services.interfaces.BankEarningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final BankEarningService bankEarningService;


    public AdminController(AdminService adminService, BankEarningService bankEarningService ) {
        this.adminService = adminService;
        this.bankEarningService = bankEarningService;

    }

    @PostMapping("/create")
    public ResponseEntity<AdminResponse> createAdmin(Authentication authentication, @Valid @RequestBody AdminCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(authentication.getName(), request));
    }

    @PatchMapping("/freeze/{accountNumber}")
    public ResponseEntity<AccountResponse> freezeAccount(Authentication authentication, @PathVariable String accountNumber) {

        return ResponseEntity.ok(adminService.freezeAccount(authentication.getName(), accountNumber));
    }

    @PatchMapping("/unfreeze/{accountNumber}")
    public ResponseEntity<AccountResponse> unfreezeAccount(Authentication authentication, @PathVariable String accountNumber) {


        return ResponseEntity.ok(adminService.unfreezeAccount(authentication.getName(), accountNumber));
    }

    @PatchMapping("/update-account-status/{accountNumber}")
    public ResponseEntity<AccountResponse> updateAccountStatus(Authentication authentication, @PathVariable String accountNumber, @Valid @RequestBody AccountStatusUpdateRequest request) {


        return ResponseEntity.ok(adminService.updateAccountStatus(authentication.getName(), accountNumber, request));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(Authentication authentication, @PathVariable Long customerId) {


        return ResponseEntity.ok(adminService.getCustomerById(authentication.getName(), customerId));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(Authentication authentication) {

        return ResponseEntity.ok(adminService.getAllCustomers(authentication.getName()));
    }

    @PatchMapping("/customers/{customerId}/tier")
    public ResponseEntity<CustomerResponse> updateCustomerTier(Authentication authentication, @PathVariable Long customerId, @Valid @RequestBody CustomerTierUpdateRequest request) {
        return ResponseEntity.ok(adminService.updateCustomerTier(authentication.getName(), customerId, request));
    }

    @GetMapping("/bank-earnings")
    public ResponseEntity<List<BankEarningResponse>> getAllEarnings() {

        return ResponseEntity.ok(bankEarningService.getAllEarnings());
    }

    @GetMapping("/bank-earnings/total/{currency}")
    public ResponseEntity<BigDecimal> getTotalEarningsByCurrency(@PathVariable Currency currency) {

        return ResponseEntity.ok(bankEarningService.getTotalEarningsByCurrency(currency));
    }


}