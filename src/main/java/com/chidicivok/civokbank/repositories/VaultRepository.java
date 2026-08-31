package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.Vault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaultRepository extends JpaRepository<Vault,Long> {
    List<Vault> findByAccountAccountNumber(String accountNumber);
}
