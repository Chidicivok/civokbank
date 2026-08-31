package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.CustomerCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerTierUpdateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerCreateRequest request);
    CustomerResponse updateCustomer(String customerEmail,   CustomerUpdateRequest request);
}