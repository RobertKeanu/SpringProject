package org.store.springproject.test.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.store.springproject.controller.ShoeController;
import org.store.springproject.dto.ShoeDto;
import org.store.springproject.exception.NoShoesFoundException;
import org.store.springproject.exception.ShoeAlreadyExistsException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.model.Shoe;
import org.store.springproject.service.OrderService;
import org.store.springproject.service.ReviewService;
import org.store.springproject.service.ShoeService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShoeControllerTest {

    @Mock
    private ShoeService shoeService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ShoeController shoeController = new ShoeController(shoeService, reviewService, orderService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(shoeController)
                .build();
    }

    @Test
    void createShoe_Success() throws Exception {
        when(shoeService.addShoe("New Shoe", 42, 10, 100.0))
                .thenReturn(new Shoe("New Shoe", 42, 10, 100.0));

        mockMvc.perform(post("/create_shoe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Shoe\",\"size\":42,\"stock\":10,\"price\":100.0}"))
                .andExpect(status().isCreated());

        verify(shoeService).addShoe("New Shoe", 42, 10, 100.0);
    }
    @Test
    void createShoe_AlreadyExists() throws Exception {
        ShoeDto shoeDto = new ShoeDto("Existing Shoe", 43, 5, 150.0);
        doThrow(new ShoeAlreadyExistsException()).when(shoeService)
                .addShoe("Existing Shoe", 43, 5, 150.0);

        mockMvc.perform(post("/create_shoe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Existing Shoe\",\"size\":43,\"stock\":5,\"price\":150.0}"))
                .andExpect(status().isBadRequest());

        verify(shoeService, times(1)).addShoe("Existing Shoe", 43, 5, 150.0);
    }

    @Test
    void deleteShoe_Success() throws Exception {
        Long shoeId = 123L;
        doNothing().when(shoeService).deleteShoe(shoeId);

        mockMvc.perform(delete("/123/delete_shoe"))
                .andExpect(status().isOk());

        verify(shoeService, times(1)).deleteShoe(shoeId);
    }

//    @Test
//    void getAllShoes_Success() throws Exception {
//        // Given the service returns a list of plain strings
//        List<String> shoeNames = List.of("Shoe1", "Shoe2");
//        when(shoeService.getShoeNames()).thenReturn(shoeNames);
//
//        mockMvc.perform(get("/all_shoes"))
//                .andExpect(status().isOk())
//                // Verify the 1st array element is the string "Shoe1"
//                .andExpect(jsonPath("$[0].shoeName").value("Shoe1"))
//                // Verify the 2nd array element is the string "Shoe2"
//                .andExpect(jsonPath("$[1].shoeName").value("Shoe2"));
//
//        verify(shoeService, times(1)).getShoeNames();
//    }


    @Test
    void getAllShoes_NoShoesFound() throws Exception {
        doThrow(new NoShoesFoundException("No shoes found!"))
                .when(shoeService).getShoeNames();

        mockMvc.perform(get("/all_shoes"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShoePrice_Success() throws Exception {
        Long shoeId = 99L;
        double newPrice = 149.99;
        doReturn(1).when(shoeService).updateShoePrice(shoeId, newPrice);

        mockMvc.perform(patch("/99/update_price?newPrice=149.99"))
                .andExpect(status().isAccepted());

        verify(shoeService, times(1)).updateShoePrice(shoeId, newPrice);
    }

    @Test
    void updateShoePrice_NotFound() throws Exception {
        Long shoeId = 999L;
        double newPrice = 200.0;
        doThrow(new ShoeNotFoundException("Shoe not found")).when(shoeService)
                .updateShoePrice(shoeId, newPrice);

        mockMvc.perform(patch("/999/update_price?newPrice=200.0"))
                .andExpect(status().isNotFound());

        verify(shoeService, times(1)).updateShoePrice(shoeId, newPrice);
    }

//    @Test
//    void getShoesRange_Success() throws Exception {
//        double minPrice = 50.0, maxPrice = 150.0;
//        List<Shoe> shoes = List.of(
//                new Shoe("Shoe1", 42, 10, 60.0),
//                new Shoe("Shoe2", 44, 5, 140.0)
//        );
//
//        when(shoeService.findShoesByPriceRange(minPrice, maxPrice)).thenReturn(shoes);
//
//        mockMvc.perform(get("/get_shoes_range?minPrice=50.0&maxPrice=150.0"))
//                .andDo(print())  // <-- Add this
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].shoeName").value("Shoe1"))
//                .andExpect(jsonPath("$[1].shoeName").value("Shoe2"));
//
//
//        verify(shoeService).findShoesByPriceRange(minPrice, maxPrice);
//    }


    @Test
    void getShoesRange_NoShoesFound() throws Exception {
        double min = 200.0, max = 300.0;
        doThrow(new NoShoesFoundException("No shoes in that range"))
                .when(shoeService).findShoesByPriceRange(min, max);

        mockMvc.perform(get("/get_shoes_range?minPrice=200.0&maxPrice=300.0"))
                .andExpect(status().isNotFound());
    }
}
