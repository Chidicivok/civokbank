package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.requests.VaultCreateRequest;
import com.chidicivok.civokbank.DTOs.responses.VaultResponse;
import com.chidicivok.civokbank.services.interfaces.VaultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaults")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping("/{accountNumber}")
    public ResponseEntity<VaultResponse> createVault(Authentication authentication, @PathVariable String accountNumber, @Valid @RequestBody VaultCreateRequest request) {

        VaultResponse response = vaultService.createVault(authentication.getName(), accountNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{vaultId}")
    public ResponseEntity<VaultResponse> getVaultById(Authentication authentication, @PathVariable Long vaultId) {
        VaultResponse response = vaultService.getVaultById(authentication.getName(), vaultId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<VaultResponse>> getVaultsByAccount(Authentication authentication, @PathVariable String accountNumber) {
        List<VaultResponse> responses = vaultService.getVaultsByAccount(authentication.getName(), accountNumber);
        return ResponseEntity.ok(responses);
    }
}