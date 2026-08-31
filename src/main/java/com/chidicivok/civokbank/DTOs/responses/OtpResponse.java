package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.OtpStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OtpResponse {

    private Long otpId;

    private OtpStatus otpStatus;

    private int attemptCount;

    private String transactionReference;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime verifiedAt;
}