package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.AdminAuditLogResponse;
import com.chidicivok.civokbank.entities.AdminAuditLog;

public class AdminAuditLogMapper {

    public static AdminAuditLogResponse toResponse(AdminAuditLog auditLog) {

        AdminAuditLogResponse response = new AdminAuditLogResponse();

        response.setAuditLogId(auditLog.getAuditLogId());
        response.setAdminId(auditLog.getAdmin().getAdminId());

        response.setAdminName(auditLog.getAdmin().getFirstName() + " " + auditLog.getAdmin().getLastName());

        response.setAction(auditLog.getAction());
        response.setTargetAccountNumber(auditLog.getTargetAccountNumber());
        response.setReason(auditLog.getReason());
        response.setCreatedAt(auditLog.getCreatedAt());

        return response;
    }
}