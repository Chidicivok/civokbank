package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.requests.OtpVerificationRequest;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;
import com.chidicivok.civokbank.services.interfaces.OtpService;
import com.chidicivok.civokbank.services.interfaces.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;
    private final TransferService transferService;

    public OtpController(OtpService otpService, TransferService transferService) {
        this.otpService = otpService;
        this.transferService = transferService;
    }

    @PostMapping("/verify/{transactionReference}")
    public ResponseEntity<TransactionResponse> verifyOtp(Authentication authentication, @PathVariable String transactionReference, @Valid @RequestBody OtpVerificationRequest request) {
        otpService.verifyOtp(authentication.getName(), transactionReference, request);
        TransactionResponse response = transferService.completeExternalTransfer(authentication.getName(), transactionReference);
        return ResponseEntity.ok(response);
    }
}