package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminAuditLogRepository        extends JpaRepository<AdminAuditLog, Long> {

    @Query("""
            SELECT auditLog
            FROM AdminAuditLog auditLog
            JOIN FETCH auditLog.admin
            ORDER BY auditLog.createdAt DESC
            """)
    List<AdminAuditLog> findAllWithAdmin();

    @Query("""
            SELECT auditLog
            FROM AdminAuditLog auditLog
            JOIN FETCH auditLog.admin admin
            WHERE admin.adminId = :adminId
            ORDER BY auditLog.createdAt DESC
            """)
    List<AdminAuditLog> findAllByAdminIdWithAdmin(            @Param("adminId") Long adminId    );
}