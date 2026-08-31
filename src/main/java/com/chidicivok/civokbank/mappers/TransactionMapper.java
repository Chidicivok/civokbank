package com.chidicivok.civokbank.mappers;

import com.chidicivok.civokbank.DTOs.responses.TransactionResponse;
import com.chidicivok.civokbank.entities.Transaction;

public class TransactionMapper {

    public static TransactionResponse toResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setTransactionReference(transaction.getTransactionReference());
        response.setTransactionType(transaction.getTransactionType());
        response.setTransactionStatus(transaction.getTransactionStatus());
        response.setAmount(transaction.getAmount());
        response.setFee(transaction.getFee());
        response.setSourceCurrency(transaction.getSourceCurrency());
        response.setDestinationCurrency(transaction.getDestinationCurrency());
        response.setDestinationAmount(transaction.getDestinationAmount());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setCompletedAt(transaction.getCompletedAt());

        if (transaction.getSourceAccount() != null) {
            response.setSourceAccountNumber(transaction.getSourceAccount().getAccountNumber());
        }

        if (transaction.getDestinationAccount() != null) {
            response.setDestinationAccountNumber(transaction.getDestinationAccount().getAccountNumber());
        } else if (transaction.getExternalDestinationAccount() != null) {
            response.setDestinationAccountNumber(transaction.getExternalDestinationAccount().getAccountNumber());
        }

        return response;
    }
}