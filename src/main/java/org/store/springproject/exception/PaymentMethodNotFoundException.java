package org.store.springproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No payment method exist")
public class PaymentMethodNotFoundException extends RuntimeException{
}
