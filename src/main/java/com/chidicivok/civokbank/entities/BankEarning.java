package com.chidicivok.civokbank.entities;

import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.enums.EarningType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_earnings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankEarning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_earning_id", nullable = false)
    private Long bankEarningId;

    @Column(name = "transaction_reference", nullable = false, length = 50)
    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "earning_type", nullable = false)
    private EarningType earningType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}