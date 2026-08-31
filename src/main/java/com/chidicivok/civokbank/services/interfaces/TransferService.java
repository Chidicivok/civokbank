package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.ExternalTransferRequest;
import com.chidicivok.civokbank.DTOs.requests.InternalTransferRequest;
import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;

public interface TransferService {

    TransactionResponse internalTransfer(String customerEmail, String sourceAccountNumber, InternalTransferRequest request);

    TransactionResponse externalTransfer(String customerEmail, String sourceAccountNumber, ExternalTransferRequest request);

    TransactionResponse completeExternalTransfer(String customerEmail, String transactionReference);
}