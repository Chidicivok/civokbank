package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog,Long> {
    List<AdminAuditLog> findByAdminAdminId(Long adminId);
}
