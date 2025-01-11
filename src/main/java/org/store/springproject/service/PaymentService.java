package org.store.springproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springproject.model.PaymentMethod;
import org.store.springproject.repository.PaymentMethodRepository;
import org.store.springproject.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final ReviewRepository reviewRepository;

    public Iterable<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }
    public List<PaymentMethod> findByType(String method){
        return paymentMethodRepository.findByType(method);
    }
    public List<PaymentMethod> findPaymentMethodsByMinOrderTotalPrice(double minOrderTotalPrice){
        return paymentMethodRepository.findPaymentMethodsByMinOrderTotalPrice(minOrderTotalPrice);
    }
    public Optional<PaymentMethod> findPaymentMethodById(Long id){
        return paymentMethodRepository.findById(id);
    }
    public void createPaymentMethod(PaymentMethod paymentMethod){
        paymentMethodRepository.save(paymentMethod);
    }
}
