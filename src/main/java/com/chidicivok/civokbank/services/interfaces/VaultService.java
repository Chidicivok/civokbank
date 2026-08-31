package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.VaultCreateRequest;
import com.chidicivok.civokbank.DTOs.responses.VaultResponse;

import java.util.List;

public interface VaultService {

    VaultResponse createVault(String customerEmail, String accountNumber, VaultCreateRequest request);

    VaultResponse getVaultById(String customerEmail, Long vaultId);

    List<VaultResponse> getVaultsByAccount(String customerEmail, String accountNumber);

    VaultResponse releaseMaturedVault(Long vaultId);
}