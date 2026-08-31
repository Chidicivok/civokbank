package com.chidicivok.civokbank.security;

import com.chidicivok.civokbank.entities.Admin;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.repositories.AdminRepository;
import com.chidicivok.civokbank.repositories.CustomerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
* UserDetailsService - functional interface that acts as the user's Data Access Object utilized by DaoAuthenticationProvider
*
* loadUserByUsername() - the method provided by UserDetailsService to fetch a user and hand over to the DaoAuthenticationProvider
*
* CustomerUserDetailsService class - my custom class implementing UserDetailsService to implement the method
*
* @Service - annotate this class to Spring as belonging to the service layer
* */
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    public CustomerUserDetailsService(CustomerRepository customerRepository, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // find if a customer else set object to empty
        Customer customer = customerRepository.findByEmail(email).orElse(null);

        // if customer is found, build the UserDetailsPrincipal for user and return
        if(customer != null) {
            return User.builder().username(customer.getEmail()).password(customer.getPassword()).roles(customer.getUserRole().name()).build();
        }

        // else check if admin and if not admin throw an exception. if not exception then it must be admin and return admin principal
       Admin admin = adminRepository.findByEmail(email).orElseThrow(
               () -> new UsernameNotFoundException("User not found: Neither customer nor admin")
       );

        // return admin principal
        return User.builder().username(admin.getEmail()).password(admin.getPassword()).roles(admin.getUserRole().name()).disabled(!admin.isActive()).build();
    }
}
