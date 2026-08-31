package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.BankEarning;
import com.chidicivok.civokbank.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankEarningRepository extends JpaRepository<BankEarning,Long> {
    List<BankEarning> findByCurrency(Currency currency);
}
