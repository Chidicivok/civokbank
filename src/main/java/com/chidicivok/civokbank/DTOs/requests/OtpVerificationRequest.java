package com.chidicivok.civokbank.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationRequest {
    @NotBlank
    private String otpCode;
}