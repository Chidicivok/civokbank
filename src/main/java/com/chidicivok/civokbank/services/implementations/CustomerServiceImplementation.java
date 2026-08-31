package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.CustomerCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.enums.CustomerTier;
import com.chidicivok.civokbank.enums.UserRole;
import com.chidicivok.civokbank.exceptions.DuplicateResourceException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.CustomerMapper;
import com.chidicivok.civokbank.repositories.CustomerRepository;
import com.chidicivok.civokbank.services.interfaces.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    // use injection for final instance variables
    public CustomerServiceImplementation(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // allow all users to create account as customer
    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest request) {

        // ensure email remains unique
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("This email \"" + request.getEmail() + "\" is already in use by another customer");
        }

        // ensure phone number remains unique
        if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("This phone number \"" + request.getPhoneNumber() + "\" is already in use by another customer");
        }

        // new customer
        Customer newCustomer = new Customer();

        // enter data from dto request
        newCustomer.setFirstName(request.getFirstName());
        newCustomer.setLastName(request.getLastName());
        newCustomer.setEmail(request.getEmail());
        newCustomer.setPhoneNumber(request.getPhoneNumber());
        newCustomer.setPassword(passwordEncoder.encode(request.getPassword()));

        // all new user defaulted to standard and customer role
        newCustomer.setCustomerTier(CustomerTier.STANDARD);
        newCustomer.setUserRole(UserRole.CUSTOMER);

        // save new customer
        Customer savedCustomer = customerRepository.save(newCustomer);

        // return response based on saved data
        return CustomerMapper.toResponse(savedCustomer);
    }

    /*
     * Only allow authenticated current customer to access their own account
     * Use Authentication Object to fetch the principal name - email
     * */
    @Override
    public CustomerResponse updateCustomer(String customerEmail, CustomerUpdateRequest request) {

        // Verify that the customer to be edited exits
        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(
                () -> new UnAuthorizedPermissionException("Customer not found with email:\t" + customerEmail.toLowerCase())
        );

        // collect data
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        // save the data
        Customer updatedCustomer = customerRepository.save(customer);

        // return response
        return CustomerMapper.toResponse(updatedCustomer);
    }

}