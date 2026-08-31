package com.chidicivok.civokbank.controllers;


import com.chidicivok.civokbank.DTOs.requests.CustomerCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerTierUpdateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;
import com.chidicivok.civokbank.mappers.CustomerMapper;
import com.chidicivok.civokbank.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/")
@Validated
public class CustomerController {

    /*
    * Not interface but reference to any class that implements this interface
    * */
    private final CustomerService customerService;

    /*
    * Dependency injection - providing the object of the class from outside that class
    * The controller is dependent on the Interface,
    * Spring creates the reference for the class implementing the interface and injects it to our controller to use
    * */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // create customer
    @PostMapping("register")
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    // update customer
    @PutMapping("update-customer")
    public ResponseEntity<CustomerResponse> updateCustomer(Authentication authentication,  @Valid @RequestBody CustomerUpdateRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(authentication.getName(), request));
    }

}
