package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.requests.OtpVerificationRequest;
import com.chidicivok.civokbank.DTOs.responses.OtpResponse;
import com.chidicivok.civokbank.entities.Customer;
import com.chidicivok.civokbank.entities.Otp;
import com.chidicivok.civokbank.entities.Transaction;
import com.chidicivok.civokbank.enums.NotificationType;
import com.chidicivok.civokbank.enums.OtpStatus;
import com.chidicivok.civokbank.enums.TransactionStatus;
import com.chidicivok.civokbank.exceptions.InvalidArgumentException;
import com.chidicivok.civokbank.exceptions.ResourceNotFoundException;
import com.chidicivok.civokbank.exceptions.UnAuthorizedPermissionException;
import com.chidicivok.civokbank.mappers.OtpMapper;
import com.chidicivok.civokbank.repositories.OtpRepository;
import com.chidicivok.civokbank.repositories.TransactionRepository;
import com.chidicivok.civokbank.services.interfaces.NotificationService;
import com.chidicivok.civokbank.services.interfaces.OtpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpServiceImplementation implements OtpService {

    private static final int MAX_OTP_ATTEMPTS = 3;

    private final OtpRepository otpRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public OtpServiceImplementation(OtpRepository otpRepository, TransactionRepository transactionRepository, NotificationService notificationService) {
        this.otpRepository = otpRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }


    // GENERATE OTP
    @Override
    @Transactional
    public OtpResponse generateOtp(String transactionReference) {

        // find transaction needing otp
        Transaction transaction = transactionRepository.findByTransactionReference(transactionReference).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found")
        );

        // ensure transaction is awaiting otp
        if (transaction.getTransactionStatus() != TransactionStatus.AWAITING_OTP) {
            throw new InvalidArgumentException("This transaction is not awaiting OTP verification");
        }
        // generate otp code
        String otpCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        // create otp
        Otp otp = new Otp();

        otp.setOtpCode(otpCode);
        otp.setOtpStatus(OtpStatus.PENDING);
        otp.setAttemptCount(0);
        otp.setTransaction(transaction);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        Otp savedOtp = otpRepository.save(otp);

        Customer customer = transaction.getSourceAccount().getCustomer();

        // notification for that customer
        notificationService.createNotification(customer, NotificationType.OTP, "Your Civok OTP is " + otpCode);

        return OtpMapper.toResponse(savedOtp);
    }


    // verify otp
    @Override
    @Transactional
    public void verifyOtp(String customerEmail, String transactionReference, OtpVerificationRequest request) {

        // check for otp using transaction reference
        Otp otp = otpRepository.findByTransactionTransactionReference(transactionReference).orElseThrow(
                () -> new ResourceNotFoundException("OTP not found")
        );

        Transaction transaction = otp.getTransaction();


        // ensure transaction belongs to authenticated user
        if (!transaction.getSourceAccount().getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new UnAuthorizedPermissionException("You are not authorized to verify this OTP");
        }

        // ensure transaction is still awaiting otp
        if (transaction.getTransactionStatus() != TransactionStatus.AWAITING_OTP) {
            throw new IllegalStateException("This transaction is not awaiting OTP verification");
        }

        // check if otp has been used
        if (otp.getOtpStatus() == OtpStatus.VERIFIED) {
            throw new IllegalStateException("OTP has already been used");
        }

        // check otp status
        if (otp.getOtpStatus() == OtpStatus.FAILED) {
            throw new IllegalStateException("OTP verification has already failed");
        }

        // check if otp has expired
        if (otp.getOtpStatus() == OtpStatus.EXPIRED) {
            throw new IllegalStateException("OTP has expired");
        }


        // check otp expiration time
        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            // force set to expire
            otp.setOtpStatus(OtpStatus.EXPIRED);
            // save expired status
            otpRepository.save(otp);

            throw new InvalidArgumentException("OTP has expired");
        }

        // verify this is the otp for this particular transaction
        if (!otp.getOtpCode().equals(request.getOtpCode())) {
            // increment attempts
            int newAttemptCount = otp.getAttemptCount() + 1;

            otp.setAttemptCount(newAttemptCount);

            if (newAttemptCount >= MAX_OTP_ATTEMPTS) {
                otp.setOtpStatus(OtpStatus.FAILED);
                otpRepository.save(otp);
                throw new InvalidArgumentException("Maximum OTP attempts exceeded");
            }


            otpRepository.save(otp);

            int attemptsRemaining = MAX_OTP_ATTEMPTS - newAttemptCount;

            throw new InvalidArgumentException("Invalid OTP. Attempts remaining: " + attemptsRemaining);
        }


        // else correct otp , verify
        otp.setOtpStatus(OtpStatus.VERIFIED);
        otp.setVerifiedAt(LocalDateTime.now());
        otpRepository.save(otp);
    }
}