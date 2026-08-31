package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.VaultCreateRequest;
import com.chidicivok.civokbank.DTOs.responses.VaultResponse;
import com.chidicivok.civokbank.entities.Account;
import com.chidicivok.civokbank.entities.Vault;
import com.chidicivok.civokbank.enums.AccountStatus;
import com.chidicivok.civokbank.enums.CustomerTier;
import com.chidicivok.civokbank.enums.VaultStatus;
import com.chidicivok.civokbank.exceptions.InvalidArgumentException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.VaultMapper;
import com.chidicivok.civokbank.repositories.AccountRepository;
import com.chidicivok.civokbank.repositories.VaultRepository;
import com.chidicivok.civokbank.services.interfaces.VaultService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaultServiceImplementation implements VaultService {

    private final VaultRepository vaultRepository;
    private final AccountRepository accountRepository;

    public VaultServiceImplementation(VaultRepository vaultRepository, AccountRepository accountRepository) {
        this.vaultRepository = vaultRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    @Transactional
    public VaultResponse createVault(String customerEmail, String accountNumber, VaultCreateRequest request) {

        Account account = getOwnedAccount(customerEmail, accountNumber);

        // only active users can create vaults
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidArgumentException("Account is not active");
        }


        if (account.getCustomer().getCustomerTier() != CustomerTier.PREMIUM) {
            throw new UnAuthorizedPermissionException("Vault is only available to premium customers");
        }


        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidArgumentException("Insufficient balance");
        }


        BigDecimal interestRate = determineInterestRate(request.getLockDurationDays());

        BigDecimal expectedInterest = request.getAmount().multiply(interestRate);


        LocalDateTime lockedAt = LocalDateTime.now();
        LocalDateTime maturityAt = lockedAt.plusDays(request.getLockDurationDays());


        // deduct from account
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        // save new balance
        accountRepository.save(account);

        // create vault
        Vault vault = new Vault();

        vault.setPrincipalAmount(request.getAmount());
        vault.setInterestRate(interestRate);
        vault.setExpectedInterest(expectedInterest);
        vault.setLockedAt(lockedAt);
        vault.setMaturityAt(maturityAt);
        vault.setAccount(account);
        vault.setVaultStatus(VaultStatus.ACTIVE);


        Vault savedVault = vaultRepository.save(vault);


        return VaultMapper.toResponse(savedVault);
    }


    @Override
    public VaultResponse getVaultById(String customerEmail, Long vaultId) {

        Vault vault = findVault(vaultId);

        validateVaultOwnership(customerEmail, vault);
        return VaultMapper.toResponse(vault);
    }


    @Override
    public List<VaultResponse> getVaultsByAccount(String customerEmail, String accountNumber) {

        getOwnedAccount(customerEmail, accountNumber);

        return vaultRepository.findByAccountAccountNumber(accountNumber)
                .stream()
                .map(VaultMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public VaultResponse releaseMaturedVault(Long vaultId) {

        Vault vault = findVault(vaultId);


        if (vault.getVaultStatus() == VaultStatus.RELEASED) {
            throw new InvalidArgumentException("Vault has already been released");
        }


        if (LocalDateTime.now().isBefore(vault.getMaturityAt())) {
            throw new InvalidArgumentException("Vault has not reached maturity");
        }


        Account account = vault.getAccount();

        BigDecimal maturityAmount = vault.getPrincipalAmount().add(vault.getExpectedInterest());

        account.setBalance(account.getBalance().add(maturityAmount));
        vault.setVaultStatus(VaultStatus.RELEASED);

        accountRepository.save(account);

        Vault releasedVault = vaultRepository.save(vault);

        return VaultMapper.toResponse(releasedVault);
    }


    // helper methods
    private Account getOwnedAccount(String customerEmail, String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account not found")
        );

        if (!account.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new UnAuthorizedPermissionException("You are not authorized to access this account");
        }


        return account;
    }


    private Vault findVault(Long vaultId) {
        return vaultRepository.findById(vaultId).orElseThrow(
                () -> new ResourceNotFoundException("Vault not found")
        );
    }


    private void validateVaultOwnership(String customerEmail, Vault vault) {

        if (!vault.getAccount().getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new UnAuthorizedPermissionException("You are not authorized to access this vault");
        }
    }


    private BigDecimal determineInterestRate(Integer lockDurationDays) {

        return switch (lockDurationDays) {
            case 30 -> new BigDecimal("0.02");
            case 90 -> new BigDecimal("0.05");
            case 180 -> new BigDecimal("0.08");
            case 365 -> new BigDecimal("0.15");
            default -> throw new IllegalArgumentException("Invalid vault lock duration");
        };
    }
}