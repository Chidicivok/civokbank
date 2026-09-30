package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.responses.AdminAuditLogResponse;

import java.util.List;

public interface AdminAuditLogService {

    List<AdminAuditLogResponse> getAllAuditLogs(String adminEmail);

    List<AdminAuditLogResponse> getAuditLogsByAdmin(String adminEmail, Long adminId);
}