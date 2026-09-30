package com.chidicivok.civokbank.security;

import com.chidicivok.civokbank.DTOs.responses.AbstractEmailVerifierApiResponse;
import com.chidicivok.civokbank.exceptions.ExternalApiFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class EmailVerificationService {

    private final RestClient restClient;


    public EmailVerificationService() {
        this.restClient = RestClient.create("https://emailreputation.abstractapi.com");
    }


    @Value("${abstract.email.api-key}")
    private String apiKey;

    public AbstractEmailVerifierApiResponse verifyEmail(String email) {

        try {

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/")
                            .queryParam("api_key", apiKey)
                            .queryParam("email", email)
                            .build()
                    ).retrieve()
                    .body(AbstractEmailVerifierApiResponse.class);

        } catch (RestClientException e) {
            throw new ExternalApiFailureException("Could not verify the email");
        }


    }


    public boolean isEmailValid(String email) {

        AbstractEmailVerifierApiResponse response = verifyEmail(email);


        boolean isStatusDeliverable = "deliverable".equalsIgnoreCase(response.emailDeliverability().status());
        boolean isStatusDetailValid = "valid_email".equalsIgnoreCase(response.emailDeliverability().statusDetail());

        return isStatusDeliverable
                && isStatusDetailValid
                && response.emailDeliverability().isFormatValid()
                && response.emailDeliverability().isSmtpValid()
                && !response.emailQuality().isDisposable();

    }


}
