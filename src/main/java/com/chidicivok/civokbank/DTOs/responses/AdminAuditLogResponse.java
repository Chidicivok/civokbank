package com.chidicivok.civokbank.DTOs.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminAuditLogResponse {

    private Long auditLogId;

    private Long adminId;

    private String adminName;

    private String action;

    private String targetAccountNumber;

    private String reason;

    private LocalDateTime createdAt;
}