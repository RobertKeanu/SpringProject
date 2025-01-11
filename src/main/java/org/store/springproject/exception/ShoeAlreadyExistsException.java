package org.store.springproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Shoe already exists")
public class ShoeAlreadyExistsException extends RuntimeException{
}
