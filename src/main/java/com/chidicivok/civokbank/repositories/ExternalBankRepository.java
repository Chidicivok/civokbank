package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.ExternalBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalBankRepository extends JpaRepository<ExternalBank,Long> {
    Optional<ExternalBank> findByBankCode(String bankCode);
}
