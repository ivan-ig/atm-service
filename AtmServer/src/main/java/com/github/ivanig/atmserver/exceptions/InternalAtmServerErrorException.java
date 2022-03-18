package com.github.ivanig.atmserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalAtmServerErrorException extends RuntimeException {

    public InternalAtmServerErrorException(String message) {
        super(message);
    }
}
