package com.keremcengiz0.devbankapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CitiesAlreadyExistException extends RuntimeException {
    public CitiesAlreadyExistException(String message) {
        super(message);
    }
}
