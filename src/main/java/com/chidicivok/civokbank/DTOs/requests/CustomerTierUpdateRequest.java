package com.chidicivok.civokbank.DTOs.requests;

import com.chidicivok.civokbank.enums.CustomerTier;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerTierUpdateRequest {
    @NotNull
    private CustomerTier customerTier;
}