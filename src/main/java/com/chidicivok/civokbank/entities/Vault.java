package com.chidicivok.civokbank.entities;

import com.chidicivok.civokbank.enums.VaultStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vaults")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vault_id", nullable = false)
    private Long vaultId;

    @Column(name = "principal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "expected_interest", nullable = false, precision = 19, scale = 2)
    private BigDecimal expectedInterest;

    @Column(name = "locked_at", nullable = false)
    private LocalDateTime lockedAt;

    @Column(name = "maturity_at", nullable = false)
    private LocalDateTime maturityAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "vault_status", nullable = false)
    private VaultStatus vaultStatus;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}