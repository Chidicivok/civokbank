package com.chidicivok.civokbank.exceptions;

public class UnAuthorizedPermissionException extends RuntimeException {
    public UnAuthorizedPermissionException(String message) {
        super(message);
    }
}
