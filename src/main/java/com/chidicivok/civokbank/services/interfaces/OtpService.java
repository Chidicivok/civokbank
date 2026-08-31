package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.OtpVerificationRequest;
import com.chidicivok.civokbank.DTOs.responses.OtpResponse;

public interface OtpService {

    OtpResponse generateOtp (String transactionReference);

    void verifyOtp(String customerEmail, String transactionReference, OtpVerificationRequest request);
}