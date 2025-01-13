package org.store.springproject.controller;

import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.springproject.dto.OrderRequestDto;
import org.store.springproject.exception.NoOrderFoundException;
import org.store.springproject.exception.PaymentMethodNotFoundException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.exception.UserNotFoundException;
import org.store.springproject.model.Order;
import org.store.springproject.model.Shoe;
import org.store.springproject.service.OrderService;
import org.store.springproject.service.ShoeService;
import org.store.springproject.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ShoeService shoeService;

    @PostMapping("/create-order")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequestDto orderDto) {
        try {
            Order order = orderService.placeOrder(orderDto.getUsername(), orderDto.getShoeIds(), orderDto.getPaymentMethodId());
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (ShoeNotFoundException | PaymentMethodNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/{orderId}/delete-order-by-id", method = RequestMethod.DELETE)
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer orderId)
    {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        catch (NoOrderFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @RequestMapping(value = "/{shoeId}/{paymentId}/get-by-shoe-payment", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getByShoeIdPaymentId(@PathVariable Long shoeId, @PathVariable Long paymentId)
    {
        List<Order> orderService1 = orderService.findByShoeIdAndPaymentService(shoeId, paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(orderService1);
    }
}
