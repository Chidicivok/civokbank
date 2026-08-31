package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;
import com.chidicivok.civokbank.entities.Customer;

public class CustomerMapper {

    public static CustomerResponse toResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setCustomerTier(customer.getCustomerTier());
        response.setUserRole(customer.getUserRole());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }
}