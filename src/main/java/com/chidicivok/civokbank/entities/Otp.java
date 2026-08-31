package com.chidicivok.civokbank.entities;

import com.chidicivok.civokbank.enums.OtpStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id", nullable = false)
    private Long otpId;

    @Column(name = "otp_code", nullable = false, length = 100)
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_status", nullable = false)
    private OtpStatus otpStatus;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}