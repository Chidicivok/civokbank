package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.CustomerTier;
import com.chidicivok.civokbank.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerResponse {

    private Long customerId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private CustomerTier customerTier;

    private UserRole userRole;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}