package com.github.ivanig.bankserver.exceptions;

public class InvalidPinCodeException extends RuntimeException {

    public InvalidPinCodeException(String message) {
        super(message);
    }
}
