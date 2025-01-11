package org.store.springproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Shoe not found")
public class ShoeNotFoundException extends RuntimeException {
    public ShoeNotFoundException(String message) {
        super(message);
    }
}