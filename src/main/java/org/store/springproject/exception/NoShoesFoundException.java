package org.store.springproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No shoes exist")
public class NoShoesFoundException extends RuntimeException {
    public NoShoesFoundException(String message) {
        super(message);
    }
}
