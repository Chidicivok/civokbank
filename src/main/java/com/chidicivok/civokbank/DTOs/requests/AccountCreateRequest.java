package com.chidicivok.civokbank.DTOs.requests;

import com.chidicivok.civokbank.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateRequest {
    @NotNull
    private Currency currency;
}