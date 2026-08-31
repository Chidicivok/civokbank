package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.requests.ExternalTransferRequest;
import com.chidicivok.civokbank.DTOs.requests.InternalTransferRequest;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;
import com.chidicivok.civokbank.services.interfaces.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/internal/{sourceAccountNumber}")
    public ResponseEntity<TransactionResponse> internalTransfer(Authentication authentication, @PathVariable String sourceAccountNumber, @Valid @RequestBody InternalTransferRequest request) {
        TransactionResponse response = transferService.internalTransfer(authentication.getName(), sourceAccountNumber, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/external/{sourceAccountNumber}")
    public ResponseEntity<TransactionResponse> externalTransfer(Authentication authentication, @PathVariable String sourceAccountNumber, @Valid @RequestBody ExternalTransferRequest request) {
        TransactionResponse response = transferService.externalTransfer(authentication.getName(), sourceAccountNumber, request);
        return ResponseEntity.ok(response);
    }
}