package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.AdminResponse;
import com.chidicivok.civokbank.entities.Admin;

public class AdminMapper {

    public static AdminResponse toResponse(Admin admin) {

        AdminResponse response = new AdminResponse();

        response.setAdminId(admin.getAdminId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setEmail(admin.getEmail());
        response.setUserRole(admin.getUserRole());
        response.setActive(admin.isActive());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());

        return response;
    }
}