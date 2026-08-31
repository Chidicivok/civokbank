package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.responses.AdminAuditLogResponse;
import com.chidicivok.civokbank.services.interfaces.AdminAuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
public class AdminAuditLogController {

    private final AdminAuditLogService adminAuditLogService;

    public AdminAuditLogController(AdminAuditLogService adminAuditLogService) {
        this.adminAuditLogService = adminAuditLogService;
    }


    @GetMapping
    public ResponseEntity<List<AdminAuditLogResponse>> getAllAuditLogs(Authentication authentication) {

        List<AdminAuditLogResponse> logs = adminAuditLogService.getAllAuditLogs(authentication.getName());
        return ResponseEntity.ok(logs);
    }


    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AdminAuditLogResponse>> getAuditLogsByAdmin(Authentication authentication, @PathVariable Long adminId) {

        List<AdminAuditLogResponse> logs = adminAuditLogService.getAuditLogsByAdmin(authentication.getName(), adminId);
        return ResponseEntity.ok(logs);
    }
}