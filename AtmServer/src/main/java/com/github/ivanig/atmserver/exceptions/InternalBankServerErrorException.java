package com.github.ivanig.atmserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalBankServerErrorException extends RuntimeException {

    public InternalBankServerErrorException() {
    }

    public InternalBankServerErrorException(String message) {
        super(message);
    }

    public InternalBankServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalBankServerErrorException(Throwable cause) {
        super(cause);
    }

    public InternalBankServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
