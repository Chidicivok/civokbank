package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.AccountStatusUpdateRequest;
import com.chidicivok.civokbank.DTOs.requests.AdminCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerTierUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.DTOs.responses.AdminResponse;
import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;

import java.util.List;

public interface AdminService {

    AdminResponse createAdmin(String authorizedAdminEmail, AdminCreateRequest request);

    AccountResponse freezeAccount(String adminEmail, String accountNumber);

    AccountResponse unfreezeAccount(String adminEmail, String accountNumber);

    AccountResponse updateAccountStatus(String adminEmail, String accountNumber, AccountStatusUpdateRequest request);

    CustomerResponse getCustomerById(String adminEmail, Long customerId);

    List<CustomerResponse> getAllCustomers(String adminEmail);

    CustomerResponse updateCustomerTier(String adminEmail, Long customerId, CustomerTierUpdateRequest request);
}