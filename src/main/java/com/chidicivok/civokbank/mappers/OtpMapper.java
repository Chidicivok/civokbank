package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.OtpResponse;
import com.chidicivok.civokbank.entities.Otp;

public class OtpMapper {

    public static OtpResponse toResponse(Otp otp) {

        OtpResponse response = new OtpResponse();

        response.setOtpId(otp.getOtpId());
        response.setOtpStatus(otp.getOtpStatus());
        response.setAttemptCount(otp.getAttemptCount());
        response.setTransactionReference(otp.getTransaction().getTransactionReference());
        response.setCreatedAt(otp.getCreatedAt());
        response.setExpiresAt(otp.getExpiresAt());
        response.setVerifiedAt(otp.getVerifiedAt());

        return response;
    }
}