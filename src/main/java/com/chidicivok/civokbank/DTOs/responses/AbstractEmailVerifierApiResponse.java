package com.chidicivok.civokbank.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AbstractEmailVerifierApiResponse(

        @JsonProperty("email_deliverability")
        EmailDeliverability emailDeliverability,

        @JsonProperty("email_quality")
        EmailQuality emailQuality
) {

    public record EmailDeliverability(

            String status,

            @JsonProperty("status_detail")
            String statusDetail,

            @JsonProperty("is_format_valid")
            boolean isFormatValid,

            @JsonProperty("is_smtp_valid")
            boolean isSmtpValid
    ) {}

    public record EmailQuality(


            @JsonProperty("is_disposable")
            boolean isDisposable
    ) {}
}