package com.chidicivok.civokbank.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
        System.out.println("This was an exception");
    }
}
