package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.ExternalTransferRequest;
import com.chidicivok.civokbank.DTOs.requests.InternalTransferRequest;
import com.chidicivok.civokbank.DTOs.responses.ExchangeRateResponse;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;
import com.chidicivok.civokbank.entities.Account;
import com.chidicivok.civokbank.entities.ExternalBankAccount;
import com.chidicivok.civokbank.entities.Transaction;
import com.chidicivok.civokbank.enums.AccountStatus;
import com.chidicivok.civokbank.enums.Currency;
import com.chidicivok.civokbank.enums.EarningType;
import com.chidicivok.civokbank.enums.TransactionStatus;
import com.chidicivok.civokbank.enums.TransactionType;
import com.chidicivok.civokbank.exceptions.InvalidArgumentException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.TransactionMapper;
import com.chidicivok.civokbank.repositories.AccountRepository;
import com.chidicivok.civokbank.repositories.ExternalBankAccountRepository;
import com.chidicivok.civokbank.repositories.TransactionRepository;
import com.chidicivok.civokbank.services.interfaces.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferServiceImplementation implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;
    private final BankingFeeService bankingFeeService;
    private final BankEarningService bankEarningService;
    private final ExternalBankAccountRepository externalBankAccountRepository;
    private final OtpService otpService;

    public TransferServiceImplementation(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            ExchangeRateService exchangeRateService,
            BankingFeeService bankingFeeService,
            BankEarningService bankEarningService,
            ExternalBankAccountRepository externalBankAccountRepository,
            OtpService otpService
    ) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.exchangeRateService = exchangeRateService;
        this.bankingFeeService = bankingFeeService;
        this.bankEarningService = bankEarningService;
        this.externalBankAccountRepository = externalBankAccountRepository;
        this.otpService = otpService;
    }

    // for internal transfers
    @Override
    @Transactional
    public TransactionResponse internalTransfer(String customerEmail, String sourceAccountNumber, InternalTransferRequest request) {

        // use authentication to fetch the customer own account
        Account sourceAccount = getOwnedAccount(customerEmail, sourceAccountNumber);

        // check the destination account if in table
        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Destination account not found")
        );

        // validate that source and destination are both active
        validateAccount(sourceAccount);
        validateAccount(destinationAccount);

        // can not transfer to same account number as destination too
        if (sourceAccount.getAccountNumber().equals(destinationAccount.getAccountNumber())) {
            throw new InvalidArgumentException("Source and destination accounts cannot be the same");
        }

        // amount to be transferred
        BigDecimal amount = request.getAmount();

        // get transaction fee based on the sender's customer tier
        BigDecimal transactionFee = bankingFeeService.getTransactionFee(sourceAccount.getCustomer().getCustomerTier());

        // if source is usd - calculate the fee in usd | civok charges in ngn primarily
        if (sourceAccount.getCurrency() == Currency.USD) {
            // get exchange rate from ngn to usd e.g 0.00074- so that if we want to charge 50 ngn we convert 50 ngn to usd
            ExchangeRateResponse feeExchangeRate = exchangeRateService.getExchangeRate(Currency.NGN, Currency.USD);
            // convert the transaction fee to usd
            transactionFee = transactionFee.multiply(feeExchangeRate.getRate());
        }

        // amount to be transferred
        BigDecimal destinationAmount = amount;

        // fx commission initialized to zero
        BigDecimal fxCommission = BigDecimal.ZERO;

        // foreign exchange transfer
        if (sourceAccount.getCurrency() != destinationAccount.getCurrency()) {
            // get exchange rate
            ExchangeRateResponse exchangeRate = exchangeRateService.getExchangeRate(sourceAccount.getCurrency(), destinationAccount.getCurrency());
            // convert the amount to be transferred using the exchange rate
            BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());
            // calculate civok bank commission
            fxCommission = bankingFeeService.calculateFxCommission(convertedAmount);
            // the commission is collected from the transaction itself hence subtract
            destinationAmount = convertedAmount.subtract(fxCommission);
        }

        // total debit for transfer internal from source
        BigDecimal totalSourceDebit = amount.add(transactionFee);

        // check if balance is sufficient for this transaction
        if (sourceAccount.getBalance().compareTo(totalSourceDebit) < 0) {
            throw new InvalidArgumentException("Your account balance is insufficient");
        }

        // debit the source account
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(totalSourceDebit));

        // credit the source amount
        destinationAccount.setBalance(destinationAccount.getBalance().add(destinationAmount));

        /*
        * The next 2 lines to save the balance
        *
        * However, they can be ignored since we used JPA repository to fetch them,
        * That means they are managed instances and JPA will detect the change in state and affect the database likewise via Dirty Checking
        *
        * */
//        accountRepository.save(sourceAccount);
//        accountRepository.save(destinationAccount);

        // create transaction
        Transaction transaction = new Transaction();

        // generate transaction reference
        transaction.setTransactionReference(generateTransactionReference());
        transaction.setTransactionType(TransactionType.INTERNAL_TRANSFER);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setAmount(amount);
        transaction.setFee(transactionFee);
        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setDestinationCurrency(destinationAccount.getCurrency());
        transaction.setDestinationAmount(destinationAmount);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);

        // save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // record civok bank earnings from this transaction
        bankEarningService.recordEarning(savedTransaction.getTransactionReference(), EarningType.TRANSACTION_FEE, transactionFee, sourceAccount.getCurrency());

        // if fx commission is greater than zero then foreign exchange occurred
        if (fxCommission.compareTo(BigDecimal.ZERO) > 0) {
            // bank earning with foreign exchange recorded
            bankEarningService.recordEarning(savedTransaction.getTransactionReference(), EarningType.FX_COMMISSION, fxCommission, destinationAccount.getCurrency());
        }

        return TransactionMapper.toResponse(savedTransaction);
    }


    // initiate external transfer
    @Override
    @Transactional
    public TransactionResponse externalTransfer(String customerEmail, String sourceAccountNumber, ExternalTransferRequest request) {

        // verify user owns the account to be debited
        Account sourceAccount = getOwnedAccount(customerEmail, sourceAccountNumber);

        // validate the source is active
        validateAccount(sourceAccount);

        // check if the external bank account exits by both the bank code and account number
        ExternalBankAccount destinationAccount = externalBankAccountRepository.findByExternalBankBankCodeAndAccountNumber(request.getBankCode(), request.getDestinationAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("External bank account not found")
        );

        // amount to be transferred
        BigDecimal amount = request.getAmount();

        // transaction fee
        BigDecimal transactionFee = bankingFeeService.getExternalTransferFee();

        // if sender is usd
        if (sourceAccount.getCurrency() == Currency.USD) {
            ExchangeRateResponse feeExchangeRate = exchangeRateService.getExchangeRate(Currency.NGN, Currency.USD);
            transactionFee = transactionFee.multiply(feeExchangeRate.getRate());
        }

        BigDecimal destinationAmount = amount;

        // check if foreign exchange
        if (sourceAccount.getCurrency() != destinationAccount.getCurrency()) {
            ExchangeRateResponse exchangeRate = exchangeRateService.getExchangeRate(sourceAccount.getCurrency(), destinationAccount.getCurrency());
            BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());
            BigDecimal fxCommission = bankingFeeService.calculateExternalFxCommission(convertedAmount);
            destinationAmount = convertedAmount.subtract(fxCommission);
        }

        BigDecimal totalSourceDebit = amount.add(transactionFee);

        // is the source sufficient for transaction
        if (sourceAccount.getBalance().compareTo(totalSourceDebit) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // create transaction
        Transaction transaction = new Transaction();

        transaction.setTransactionReference(generateTransactionReference());
        transaction.setTransactionType(TransactionType.EXTERNAL_TRANSFER);
        transaction.setTransactionStatus(TransactionStatus.AWAITING_OTP);
        transaction.setAmount(amount);
        transaction.setFee(transactionFee);
        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setDestinationCurrency(destinationAccount.getCurrency());
        transaction.setDestinationAmount(destinationAmount);
        transaction.setSourceAccount(sourceAccount);
        transaction.setExternalDestinationAccount(destinationAccount);

        Transaction savedTransaction = transactionRepository.save(transaction);

        otpService.generateOtp(savedTransaction.getTransactionReference());

        return TransactionMapper.toResponse(savedTransaction);
    }


    // complete external transfers using otp and transaction reference
    @Override
    @Transactional
    public TransactionResponse completeExternalTransfer(String customerEmail, String transactionReference) {

        Transaction transaction = transactionRepository.findByTransactionReference(transactionReference).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found")
        );

        if (!transaction.getSourceAccount().getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new UnAuthorizedPermissionException("This is not your account");
        }

        if (transaction.getTransactionStatus() != TransactionStatus.AWAITING_OTP) {
            throw new InvalidArgumentException("Transaction is not awaiting OTP verification");
        }


        // fetch source account
        Account sourceAccount = transaction.getSourceAccount();

        // fetch destination account
        ExternalBankAccount destinationAccount = transaction.getExternalDestinationAccount();

        // validate internal source
        validateAccount(sourceAccount);

        BigDecimal totalSourceDebit = transaction.getAmount().add(transaction.getFee());


        if (sourceAccount.getBalance().compareTo(totalSourceDebit) < 0) {
            throw new InvalidArgumentException("Insufficient balance");
        }

        // debit source
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(totalSourceDebit));

        // credit destination
        destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getDestinationAmount()));

        // save new source account balance
        accountRepository.save(sourceAccount);

        // save external bank account balance
        externalBankAccountRepository.save(destinationAccount);

        // record bank earning
        bankEarningService.recordEarning(transaction.getTransactionReference(), EarningType.TRANSACTION_FEE, transaction.getFee(), sourceAccount.getCurrency());

        // if fx exchange transfer
        if (transaction.getSourceCurrency() != transaction.getDestinationCurrency()) {

            // get ex rate
            ExchangeRateResponse exchangeRate = exchangeRateService.getExchangeRate(transaction.getSourceCurrency(), transaction.getDestinationCurrency());
            // convert amount using ex rate
            BigDecimal convertedAmount = transaction.getAmount().multiply(exchangeRate.getRate());
            // calc commission
            BigDecimal fxCommission = bankingFeeService.calculateExternalFxCommission(convertedAmount);
            // record bank earning
            bankEarningService.recordEarning(transaction.getTransactionReference(), EarningType.FX_COMMISSION, fxCommission, transaction.getDestinationCurrency());
        }

        // update transaction to successful
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setCompletedAt(LocalDateTime.now());

        // save transaction
        Transaction completedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.toResponse(completedTransaction);
    }


    // helper methods
    private Account getOwnedAccount(String customerEmail, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account \"" + accountNumber + "\" not found")
        );

        if (!account.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new UnAuthorizedPermissionException("This account does not belong to you");
        }

        return account;
    }


    private void validateAccount(Account account) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidArgumentException("Account is not active");
        }
    }

    private String generateTransactionReference() {
        return "CIV-" + LocalDate.now() + UUID.randomUUID();
    }
}