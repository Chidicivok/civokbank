package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.responses.AdminAuditLogResponse;
import com.chidicivok.civokbank.entities.Admin;

import java.time.LocalDate;
import java.util.List;

public interface AdminAuditLogService {

    void logAction(Admin admin, String action, String targetUser, String targetAccountNumber, String reason, LocalDate date);

    List<AdminAuditLogResponse> getAllAuditLogs(String adminEmail);

    List<AdminAuditLogResponse> getAuditLogsByAdmin(String adminEmail, Long adminId);
}