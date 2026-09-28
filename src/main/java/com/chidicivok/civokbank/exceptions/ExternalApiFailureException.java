package com.chidicivok.civokbank.exceptions;

public class ExternalApiFailureException extends RuntimeException {
    public ExternalApiFailureException(String message) {
        super(message);
    }
}
