package com.chidicivok.civokbank.DTOs.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalBankResponse {

    private Long externalBankId;

    private String bankName;

    private String bankCode;
}