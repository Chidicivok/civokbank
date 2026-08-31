package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.VaultResponse;
import com.chidicivok.civokbank.entities.Vault;

public class VaultMapper {

    public static VaultResponse toResponse(Vault vault) {

        VaultResponse response = new VaultResponse();

        response.setVaultId(vault.getVaultId());
        response.setPrincipalAmount(vault.getPrincipalAmount());
        response.setInterestRate(vault.getInterestRate());
        response.setExpectedInterest(vault.getExpectedInterest());
        response.setLockedAt(vault.getLockedAt());
        response.setMaturityAt(vault.getMaturityAt());
        response.setAccountId(vault.getAccount().getAccountId());
        response.setVaultStatus(vault.getVaultStatus());

        return response;
    }
}