package org.store.springproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.springproject.exception.PaymentMethodExistsException;
import org.store.springproject.exception.PaymentMethodNotFoundException;
import org.store.springproject.model.PaymentMethod;
import org.store.springproject.service.PaymentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/get-all-payment-methods")
    public ResponseEntity<Iterable<PaymentMethod>> getAllPaymentMethods() {
        Iterable<PaymentMethod> paymentMethods = paymentService.findAll();
        return ResponseEntity.ok(paymentMethods);
    }
    @PostMapping("/create-payment-method")
    public ResponseEntity<Void> createNewPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        try{
            paymentService.createPaymentMethod(paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch(PaymentMethodExistsException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
    @RequestMapping(value = "/get-by-min-total-price", method = RequestMethod.GET)
    public ResponseEntity<List<PaymentMethod>> getByMinTotalPrice(double minOrderTotalPrice) {
        try{
            paymentService.findPaymentMethodsByMinOrderTotalPrice(minOrderTotalPrice);
            return ResponseEntity.ok(paymentService.findPaymentMethodsByMinOrderTotalPrice(minOrderTotalPrice));
        }
        catch(PaymentMethodNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
