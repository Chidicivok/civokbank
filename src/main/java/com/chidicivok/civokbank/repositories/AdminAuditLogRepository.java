package com.chidicivok.civokbank.repositories;

import com.chidicivok.civokbank.entities.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {

    /*
    * JPA findAll() - will run multiple times since all admin logs are stored in same place
    *
    * So basically the first query will collect all logs
    * Select * from audit_logs;
    *
    * Then Jpa will have to run it through a loop to sieve for each admin
    * for(AuditLog log: audit_logs) {
    *       SOUT(log.getAdmin)
    * }
    *
    * the loop will run multiple times for how many records you have
    * hence an N+1 problem - where 1 is the initial select all logs, and N is the number of sub queries
    *
    * *****************************************************************************
    *
    * To avoid this issue and improve performance - use collect the respective admins when collecting logs
    * This will bundle the whole operation into a single SQL query
    * */
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
    List<AdminAuditLog> findAllByAdminIdWithAdmin(@Param("adminId") Long adminId);
}