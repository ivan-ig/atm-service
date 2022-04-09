package com.github.ivanig.atmserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class WaitingTimeElapsedException extends RuntimeException {

    public WaitingTimeElapsedException() {
    }

    public WaitingTimeElapsedException(String message) {
        super(message);
    }

    public WaitingTimeElapsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WaitingTimeElapsedException(Throwable cause) {
        super(cause);
    }

    public WaitingTimeElapsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}