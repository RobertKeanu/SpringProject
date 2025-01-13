package org.store.springproject.test.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.store.springproject.controller.OrderController;
import org.store.springproject.dto.OrderRequestDto;
import org.store.springproject.exception.NoOrderFoundException;
import org.store.springproject.exception.PaymentMethodNotFoundException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.exception.UserNotFoundException;
import org.store.springproject.model.Order;
import org.store.springproject.service.OrderService;
import org.store.springproject.service.ShoeService;
import org.store.springproject.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private ShoeService shoeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        OrderController orderController = new OrderController(orderService, userService, shoeService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();
    }

    @Test
    void placeOrder_Success() throws Exception {
        OrderRequestDto dto = new OrderRequestDto("john_doe", List.of(1L, 2L), 10L);

        Order mockOrder = new Order();
        mockOrder.setId(999);
        mockOrder.setPrice(120.0);

        when(orderService.placeOrder("john_doe", List.of(1L, 2L), 10L)).thenReturn(mockOrder);
        mockMvc.perform(post("/create-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"shoeIds\":[1,2],\"paymentMethodId\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(999))
                .andExpect(jsonPath("$.price").value(120.0));

        verify(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);
    }

    @Test
    void placeOrder_BadRequest_ShoeNotFound() throws Exception {
        doThrow(new ShoeNotFoundException("Shoe not found"))
                .when(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);

        mockMvc.perform(post("/create-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"shoeIds\":[1,2],\"paymentMethodId\":10}"))
                .andExpect(status().isBadRequest());

        verify(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);
    }

    @Test
    void placeOrder_BadRequest_PaymentNotFound() throws Exception {
        doThrow(new PaymentMethodNotFoundException())
                .when(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);

        mockMvc.perform(post("/create-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"shoeIds\":[1,2],\"paymentMethodId\":10}"))
                .andExpect(status().isBadRequest());

        verify(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);
    }

    @Test
    void placeOrder_BadRequest_UserNotFound() throws Exception {
        doThrow(new UserNotFoundException())
                .when(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);

        mockMvc.perform(post("/create-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"shoeIds\":[1,2],\"paymentMethodId\":10}"))
                .andExpect(status().isBadRequest());

        verify(orderService).placeOrder("john_doe", List.of(1L, 2L), 10L);
    }

    @Test
    void cancelOrder_Success() throws Exception {
        Integer orderId = 123;
        doNothing().when(orderService).cancelOrder(orderId);

        mockMvc.perform(delete("/123/delete-order-by-id"))
                .andExpect(status().isAccepted());

        verify(orderService).cancelOrder(orderId);
    }

    @Test
    void cancelOrder_NotFound() throws Exception {
        Integer orderId = 999;
        doThrow(new NoOrderFoundException())
                .when(orderService).cancelOrder(orderId);

        mockMvc.perform(delete("/999/delete-order-by-id"))
                .andExpect(status().isNotFound());

        verify(orderService).cancelOrder(orderId);
    }

    @Test
    void getByShoeIdPaymentId_Success() throws Exception {
        Long shoeId = 10L;
        Long paymentId = 20L;
        Order o1 = new Order();
        o1.setId(1);
        Order o2 = new Order();
        o2.setId(2);
        List<Order> orders = List.of(o1, o2);

        when(orderService.findByShoeIdAndPaymentService(shoeId, paymentId))
                .thenReturn(orders);

        mockMvc.perform(get("/10/20/get-by-shoe-payment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(orderService).findByShoeIdAndPaymentService(shoeId, paymentId);
    }
}
