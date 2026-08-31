package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.AccountCreateRequest;
import com.chidicivok.civokbank.DTOs.requests.DepositRequest;
import com.chidicivok.civokbank.DTOs.requests.WithdrawalRequest;
import com.chidicivok.civokbank.DTOs.responses.AccountResponse;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;
import com.chidicivok.civokbank.entities.Account;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.entities.Transaction;
import com.chidicivok.civokbank.enums.AccountStatus;
import com.chidicivok.civokbank.enums.TransactionStatus;
import com.chidicivok.civokbank.enums.TransactionType;
import com.chidicivok.civokbank.exceptions.InvalidArgumentException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.AccountMapper;
import com.chidicivok.civokbank.mappers.TransactionMapper;
import com.chidicivok.civokbank.repositories.AccountRepository;
import com.chidicivok.civokbank.repositories.CustomerRepository;
import com.chidicivok.civokbank.repositories.TransactionRepository;
import com.chidicivok.civokbank.services.interfaces.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImplementation implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImplementation(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    // only customers can create account
    @Override
    public AccountResponse createAccount(String customerEmail, AccountCreateRequest request) {

        // verify if the customer exits first
        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(
                () -> new ResourceNotFoundException("Customer with email \"" + customerEmail + "\" not found")
        );

        // create new account
        Account newAccount = new Account();

        // change generate account number -
        newAccount.setAccountNumber(generateAccountNumber());
        newAccount.setAccountStatus(AccountStatus.ACTIVE);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setCurrency(request.getCurrency());
        newAccount.setCustomer(customer);

        Account savedAccount = accountRepository.save(newAccount);

        return AccountMapper.toResponse(savedAccount);
    }

    // only customers can find their own accounts
    @Override
    public AccountResponse getAccountByNumber(String customerEmail, String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account Number: " + accountNumber + "not found")
        );

        if (!account.getCustomer().getEmail().equals(customerEmail)) {
            throw new UnAuthorizedPermissionException("This account does not belong to you");
        }

        return AccountMapper.toResponse(account);
    }


    // get all accounts of the customer
    @Override
    public List<AccountResponse> getCustomerAccounts(String customerEmail) {

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(
                () -> new ResourceNotFoundException("Customer with email \"" + customerEmail + "\" not found")
        );

        if (!customer.getEmail().equalsIgnoreCase(customerEmail)) {
            throw new UnAuthorizedPermissionException("This account does not belong to you");
        }

        return accountRepository.findByCustomerCustomerId(customer.getCustomerId())
                .stream()
                .map(AccountMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public TransactionResponse deposit(String customerEmail, String accountNumber, DepositRequest request) {

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account Number: " + accountNumber + "not found")
        );

        if (!account.getCustomer().getEmail().equals(customerEmail)) {
            throw new UnAuthorizedPermissionException("This account does not belong to you");
        }

        validateAccount(account);

        account.setBalance(account.getBalance().add(request.getAmount()));

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(generateTransactionReference());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setAmount(request.getAmount());
        transaction.setFee(BigDecimal.ZERO);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setDestinationCurrency(account.getCurrency());
        transaction.setDestinationAmount(request.getAmount());
        transaction.setDestinationAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(String customerEmail, String accountNumber, WithdrawalRequest request) {

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account Number: " + accountNumber + "not found")
        );

        // must be active account
        validateAccount(account);

        if (!account.getCustomer().getEmail().equals(customerEmail)) {
            throw new UnAuthorizedPermissionException("This account does not belong to you");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidArgumentException("Account Balance is insufficient");
        }

        // subtract from the balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        // save the new balance
        accountRepository.save(account);

        // create transaction
        Transaction transaction = new Transaction();

        transaction.setTransactionReference(generateTransactionReference());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setAmount(request.getAmount());
        transaction.setFee(BigDecimal.ZERO);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setDestinationCurrency(account.getCurrency());
        transaction.setDestinationAmount(request.getAmount());
        transaction.setSourceAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.toResponse(savedTransaction);
    }

    // helper method
    private void validateAccount(Account account) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account \"" + account.getAccountNumber() + "\" is not active. Contact Administrator");
        }
    }

    private String generateAccountNumber() {
        return String.valueOf(1000000000L + (long) (Math.random() * 9000000000L));
    }

    // generate reference for transactions
    private String generateTransactionReference() {
        return "CIV-" + UUID.randomUUID();
    }
}