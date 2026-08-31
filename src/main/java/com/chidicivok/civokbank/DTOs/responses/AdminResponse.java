package com.chidicivok.civokbank.DTOs.responses;

import com.chidicivok.civokbank.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminResponse {

    private Long adminId;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole userRole;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}