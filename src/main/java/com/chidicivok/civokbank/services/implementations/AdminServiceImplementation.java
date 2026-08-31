package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.AccountStatusUpdateRequest;
import com.chidicivok.civokbank.DTOs.requests.AdminCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.CustomerTierUpdateRequest;
import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.DTOs.responses.AdminResponse;
import com.chidicivok.civokbank.DTOs.responses.CustomerResponse;
import com.chidicivok.civokbank.entities.Account;
import com.chidicivok.civokbank.entities.Admin;
import com.chidicivok.civokbank.entities.AdminAuditLog;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.enums.AccountStatus;
import com.chidicivok.civokbank.enums.UserRole;
import com.chidicivok.civokbank.exceptions.DuplicateResourceException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.AccountMapper;
import com.chidicivok.civokbank.mappers.AdminMapper;
import com.chidicivok.civokbank.mappers.CustomerMapper;
import com.chidicivok.civokbank.repositories.AccountRepository;
import com.chidicivok.civokbank.repositories.AdminAuditLogRepository;
import com.chidicivok.civokbank.repositories.AdminRepository;
import com.chidicivok.civokbank.repositories.CustomerRepository;
import com.chidicivok.civokbank.services.interfaces.AdminService;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImplementation implements AdminService {

    private final AdminRepository adminRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AdminAuditLogRepository adminAuditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImplementation(
            AdminRepository adminRepository,
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AdminAuditLogRepository adminAuditLogRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminRepository = adminRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.adminAuditLogRepository = adminAuditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminResponse createAdmin(String authorizedAdminEmail, AdminCreateRequest request) {

        Admin authorizedAdmin = findAdmin(authorizedAdminEmail);

        if (authorizedAdmin.getUserRole() != UserRole.AUTHORIZED_ADMIN) {
            throw new UnAuthorizedPermissionException("You are not authorized to create administrators");
        }

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("This email is already in use by another admin");
        }

        Admin admin = new Admin();

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setUserRole(UserRole.ADMIN);
        admin.setActive(true);

        Admin savedAdmin = adminRepository.save(admin);

        // create log
        AdminAuditLog log = new AdminAuditLog();

        log.setAdmin(authorizedAdmin);
        log.setAction("createAdmin()");
        log.setTargetUser(savedAdmin.getEmail());
        log.setTargetAccountNumber(null);
        log.setReason("To grant admin access");
        log.setCreatedAt(LocalDateTime.now());

        adminAuditLogRepository.save(log);


        return AdminMapper.toResponse(savedAdmin);
    }

    @Override
    @Transactional
    public AccountResponse freezeAccount(String adminEmail, String accountNumber) {

        validateAdmin(adminEmail);

        Account account = findAccount(accountNumber);

        account.setAccountStatus(AccountStatus.FROZEN);

        Account savedAccount = accountRepository.save(account);


        // create log
        AdminAuditLog log = new AdminAuditLog();

        log.setAdmin(
                adminRepository.findByEmail(adminEmail).orElseThrow(() -> new ResourceNotFoundException("Admin not found"))
        );
        log.setAction("freezeAccount()");
        log.setTargetUser(                account.getCustomer().getEmail()        );
        log.setTargetAccountNumber(accountNumber);
        log.setReason("To freeze account");
        log.setCreatedAt(LocalDateTime.now());

        adminAuditLogRepository.save(log);



        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional
    public AccountResponse unfreezeAccount(String adminEmail, String accountNumber) {

        validateAdmin(adminEmail);

        Account account = findAccount(accountNumber);

        account.setAccountStatus(AccountStatus.ACTIVE);

        Account savedAccount = accountRepository.save(account);


        // create log
        AdminAuditLog log = new AdminAuditLog();

        log.setAdmin(
                adminRepository.findByEmail(adminEmail).orElseThrow(() -> new ResourceNotFoundException("Admin not found"))
        );
        log.setAction("unfreezeAccount()");
        log.setTargetUser(                account.getCustomer().getEmail()        );
        log.setTargetAccountNumber(accountNumber);
        log.setReason("To un-freeze account");
        log.setCreatedAt(LocalDateTime.now());

        adminAuditLogRepository.save(log);

        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional
    public AccountResponse updateAccountStatus(String adminEmail, String accountNumber, AccountStatusUpdateRequest request) {

        validateAdmin(adminEmail);

        Account account = findAccount(accountNumber);

        account.setAccountStatus(request.getAccountStatus());

        Account savedAccount = accountRepository.save(account);

        // create log
        AdminAuditLog log = new AdminAuditLog();

        log.setAdmin(
                adminRepository.findByEmail(adminEmail).orElseThrow(() -> new ResourceNotFoundException("Admin not found"))
        );
        log.setAction("updateAccountStatus()");
        log.setTargetUser(                account.getCustomer().getEmail()        );
        log.setTargetAccountNumber(accountNumber);
        log.setReason("To change account status");
        log.setCreatedAt(LocalDateTime.now());

        adminAuditLogRepository.save(log);

        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    public CustomerResponse getCustomerById(String adminEmail, Long customerId) {

        validateAdmin(adminEmail);

        Customer customer = findCustomer(customerId);

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers(String adminEmail) {

        validateAdmin(adminEmail);

        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerTier(String adminEmail, Long customerId, CustomerTierUpdateRequest request) {

        validateAdmin(adminEmail);

        Customer customer = findCustomer(customerId);

        customer.setCustomerTier(request.getCustomerTier());

        Customer updatedCustomer = customerRepository.save(customer);


        // create log
        AdminAuditLog log = new AdminAuditLog();

        log.setAdmin(
                adminRepository.findByEmail(adminEmail).orElseThrow(() -> new ResourceNotFoundException("Admin not found"))
        );
        log.setAction("freezeAccount()");
        log.setTargetUser(                customer.getEmail()        );
        log.setTargetAccountNumber(null );
        log.setReason("To upgrade customer tier");
        log.setCreatedAt(LocalDateTime.now());

        adminAuditLogRepository.save(log);



        return CustomerMapper.toResponse(updatedCustomer);
    }


    private Admin findAdmin(String adminEmail) {

        return adminRepository.findByEmail(adminEmail).orElseThrow(
                () -> new ResourceNotFoundException("Invalid administrator")
        );
    }

    private void validateAdmin(String adminEmail) {

        Admin admin = findAdmin(adminEmail);

        if (!admin.isActive()) {
            throw new UnAuthorizedPermissionException("Administrator account is inactive");
        }

        if (admin.getUserRole() != UserRole.ADMIN && admin.getUserRole() != UserRole.AUTHORIZED_ADMIN) {

            throw new UnAuthorizedPermissionException("You are not an administrator");
        }
    }

    private Account findAccount(String accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account not found")
        );
    }

    private Customer findCustomer(Long customerId) {

        return customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found with ID: " + customerId)
        );
    }
}