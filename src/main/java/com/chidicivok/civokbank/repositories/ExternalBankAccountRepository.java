package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.ExternalBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalBankAccountRepository extends JpaRepository<ExternalBankAccount, Long> {
    Optional<ExternalBankAccount> findByExternalBankBankCodeAndAccountNumber(String bankCode, String accountNumber);
}
