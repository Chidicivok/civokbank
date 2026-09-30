package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.OtpVerificationRequest;

public interface OtpService {

    void generateOtp (String transactionReference);

    void verifyOtp(String customerEmail, String transactionReference, OtpVerificationRequest request);
}