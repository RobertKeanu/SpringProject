package org.store.springproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springproject.exception.OrderNotFoundException;
import org.store.springproject.exception.PaymentMethodNotFoundException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.model.Order;
import org.store.springproject.model.PaymentMethod;
import org.store.springproject.model.Shoe;
import org.store.springproject.repository.OrderRepository;
import org.store.springproject.service.PaymentService;
import org.store.springproject.service.ShoeService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShoeService shoeService;
    private final PaymentService paymentService;

    public List<Order> findByMinTotalPrice(double minTotalPrice) {
        return orderRepository.findByMinTotalPrice(minTotalPrice);
    }
     public List<Order> findByShoeIdAndPaymentService(Long shoeId, Long paymentId) {
        Shoe shoe = shoeService.findById(shoeId).orElseThrow(() -> new ShoeNotFoundException("Shoe not found"));
        PaymentMethod paymentMethod = paymentService.findPaymentMethodById(paymentId).orElseThrow(PaymentMethodNotFoundException::new);
        return orderRepository.findByShoeIdAndPaymentId(shoe.getId(), (long) paymentMethod.getId()); // nu stiu daca merge
     }
     public Order placeOrder(Order order) {
        double totalPrice = order.getShoes().stream()
                .mapToDouble(Shoe::getPrice)
                .sum();
        order.setPrice(totalPrice);
        return orderRepository.save(order);
     }
     public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        orderRepository.delete(order);
     }
}
