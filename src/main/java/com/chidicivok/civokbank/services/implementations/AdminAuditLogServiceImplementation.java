package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.responses.AdminAuditLogResponse;
import com.chidicivok.civokbank.entities.Admin;
import com.chidicivok.civokbank.entities.AdminAuditLog;
import com.chidicivok.civokbank.enums.UserRole;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.AdminAuditLogMapper;
import com.chidicivok.civokbank.repositories.AdminAuditLogRepository;
import com.chidicivok.civokbank.repositories.AdminRepository;
import com.chidicivok.civokbank.services.interfaces.AdminAuditLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminAuditLogServiceImplementation implements AdminAuditLogService {

    private final AdminAuditLogRepository adminAuditLogRepository;
    private final AdminRepository adminRepository;

    public AdminAuditLogServiceImplementation(AdminAuditLogRepository adminAuditLogRepository, AdminRepository adminRepository) {
        this.adminAuditLogRepository = adminAuditLogRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public void logAction(
            Admin admin,
            String action,
            String targetUser,
            String targetAccountNumber,
            String reason,
            LocalDate date) {

        AdminAuditLog auditLog = new AdminAuditLog();

        auditLog.setAdmin(admin);
        auditLog.setAction(action);
        auditLog.setTargetUser(targetUser);
        auditLog.setTargetAccountNumber(targetAccountNumber);
        auditLog.setReason(reason);
        auditLog.setCreatedAt(LocalDateTime.now());

         adminAuditLogRepository.save(auditLog);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AdminAuditLogResponse> getAllAuditLogs(String adminEmail) {

        validateAdmin(adminEmail);

        return adminAuditLogRepository.findAllWithAdmin()
                .stream()
                .map(AdminAuditLogMapper::toResponse)
                .toList();
    }


    @Override
    public List<AdminAuditLogResponse> getAuditLogsByAdmin(String adminEmail, Long adminId) {

        validateAdmin(adminEmail);

        return adminAuditLogRepository.findAllByAdminIdWithAdmin(adminId)
                .stream()
                .map(AdminAuditLogMapper::toResponse)
                .toList();
    }


    private void validateAdmin(String adminEmail) {

        Admin admin = adminRepository.findByEmail(adminEmail).orElseThrow(
                () -> new ResourceNotFoundException("Administrator not found")
        );


        if (!admin.isActive()) {
            throw new UnAuthorizedPermissionException("Administrator account is inactive");
        }


        if (admin.getUserRole() != UserRole.ADMIN && admin.getUserRole() != UserRole.AUTHORIZED_ADMIN) {
            throw new UnAuthorizedPermissionException("You are not authorized to view audit logs");
        }
    }
}