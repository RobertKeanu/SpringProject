package org.store.springproject.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.springproject.exception.*;
import org.store.springproject.model.*;
import org.store.springproject.repository.OrderRepository;
import org.store.springproject.service.OrderService;
import org.store.springproject.service.PaymentService;
import org.store.springproject.service.ShoeService;
import org.store.springproject.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShoeService shoeService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void placeOrder_Success() {
        String username = "john_doe";
        List<Long> shoeIds = List.of(1L, 2L);
        Long paymentId = 10L;

        User user = new User();
        user.setUsername(username);

        Shoe shoe1 = new Shoe();
        shoe1.setId(1L);
        shoe1.setPrice(50.0);
        Shoe shoe2 = new Shoe();
        shoe2.setId(2L);
        shoe2.setPrice(70.0);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(10);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(shoeService.findById(1L)).thenReturn(Optional.of(shoe1));
        when(shoeService.findById(2L)).thenReturn(Optional.of(shoe2));
        when(paymentService.findPaymentMethodById(paymentId)).thenReturn(Optional.of(paymentMethod));

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order order = orderService.placeOrder(username, shoeIds, paymentId);

        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(2, order.getShoes().size());
        assertEquals(120.0, order.getPrice());
        assertEquals("PLACED", order.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void placeOrder_UserNotFound() {
        String unknownUser = "unknown_user";
        when(userService.findByUsername(unknownUser)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                orderService.placeOrder(unknownUser, List.of(1L, 2L), 10L)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_ShoeNotFound() {
        String username = "john_doe";
        when(userService.findByUsername(username)).thenReturn(Optional.of(new User()));

        when(shoeService.findById(1L)).thenReturn(Optional.of(new Shoe()));
        when(shoeService.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ShoeNotFoundException.class, () ->
                orderService.placeOrder(username, List.of(1L, 2L), 10L)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_PaymentMethodNotFound() {
        String username = "john_doe";
        when(userService.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(shoeService.findById(anyLong())).thenReturn(Optional.of(new Shoe()));
        when(paymentService.findPaymentMethodById(999L)).thenReturn(Optional.empty());

        assertThrows(PaymentMethodNotFoundException.class, () ->
                orderService.placeOrder(username, List.of(1L, 2L), 999L)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelOrder_Success() {
        Integer orderId = 100;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        orderService.cancelOrder(orderId);

        verify(orderRepository, times(1)).delete(existingOrder);
    }

    @Test
    void cancelOrder_NotFound() {
        Integer orderId = 999;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () ->
                orderService.cancelOrder(orderId)
        );
        verify(orderRepository, never()).delete(any());
    }

    @Test
    void findByShoeIdAndPaymentService_Success() {
        Long shoeId = 10L;
        Long paymentId = 20L;

        Shoe shoe = new Shoe();
        shoe.setId(shoeId);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(20);

        Order order1 = new Order();
        order1.setId(1);
        Order order2 = new Order();
        order2.setId(2);
        List<Order> mockOrders = List.of(order1, order2);

        when(shoeService.findById(shoeId)).thenReturn(Optional.of(shoe));
        when(paymentService.findPaymentMethodById(paymentId)).thenReturn(Optional.of(paymentMethod));
        when(orderRepository.findByShoeIdAndPaymentId(shoeId, (long) paymentMethod.getId()))
                .thenReturn(mockOrders);

        List<Order> result = orderService.findByShoeIdAndPaymentService(shoeId, paymentId);

        assertEquals(2, result.size());
        verify(orderRepository).findByShoeIdAndPaymentId(shoeId, (long) paymentMethod.getId());
    }

    @Test
    void findByShoeIdAndPaymentService_ShoeNotFound() {
        Long shoeId = 10L, paymentId = 20L;
        when(shoeService.findById(shoeId)).thenReturn(Optional.empty());

        assertThrows(ShoeNotFoundException.class, () ->
                orderService.findByShoeIdAndPaymentService(shoeId, paymentId)
        );
        verify(orderRepository, never()).findByShoeIdAndPaymentId(anyLong(), anyLong());
    }

    @Test
    void findByShoeIdAndPaymentService_PaymentMethodNotFound() {
        Long shoeId = 10L, paymentId = 20L;
        Shoe shoe = new Shoe();
        shoe.setId(shoeId);

        when(shoeService.findById(shoeId)).thenReturn(Optional.of(shoe));
        when(paymentService.findPaymentMethodById(paymentId)).thenReturn(Optional.empty());

        assertThrows(PaymentMethodNotFoundException.class, () ->
                orderService.findByShoeIdAndPaymentService(shoeId, paymentId)
        );
        verify(orderRepository, never()).findByShoeIdAndPaymentId(anyLong(), anyLong());
    }
}
