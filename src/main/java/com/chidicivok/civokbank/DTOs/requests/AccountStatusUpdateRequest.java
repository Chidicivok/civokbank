package com.chidicivok.civokbank.DTOs.requests;

import com.chidicivok.civokbank.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountStatusUpdateRequest {

    @NotNull
    private AccountStatus accountStatus;
}