package com.github.ivanig.bankserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPinCodeException extends RuntimeException {

    public InvalidPinCodeException(String message) {
        super(message);
    }
}
