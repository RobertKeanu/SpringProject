package org.store.springproject.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.springproject.exception.NoShoesFoundException;
import org.store.springproject.exception.ShoeAlreadyExistsException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.model.Shoe;
import org.store.springproject.repository.ShoeRepository;
import org.store.springproject.service.ShoeService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoeServiceTest {

    @Mock
    private ShoeRepository shoeRepository;

    @InjectMocks
    private ShoeService shoeService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addShoe_Success() {
        String name = "Running Shoe";
        int size = 42;
        int stock = 10;
        double price = 99.99;

        when(shoeRepository.findByShoeName(name)).thenReturn(Optional.empty());
        when(shoeRepository.save(any(Shoe.class))).thenAnswer(inv -> inv.getArgument(0));

        Shoe savedShoe = shoeService.addShoe(name, size, stock, price);

        assertNotNull(savedShoe);
        assertEquals(name, savedShoe.getShoeName());
        assertEquals(size, savedShoe.getSize());
        assertEquals(stock, savedShoe.getStock());
        assertEquals(price, savedShoe.getPrice());
        verify(shoeRepository, times(1)).findByShoeName(name);
        verify(shoeRepository, times(1)).save(any(Shoe.class));
    }

    @Test
    void addShoe_ShoeAlreadyExists() {
        String name = "Running Shoe";
        when(shoeRepository.findByShoeName(name))
                .thenReturn(Optional.of(new Shoe(name, 42, 10, 99.99)));

        assertThrows(ShoeAlreadyExistsException.class, () -> {
            shoeService.addShoe(name, 42, 10, 99.99);
        });
        verify(shoeRepository, never()).save(any());
    }

    @Test
    void findByNameAndCategory_Success() {
        String name = "Sport Shoe";
        Long categoryId = 100L;
        Shoe shoe = new Shoe(name, 45, 5, 120.0);
        when(shoeRepository.findByNameAndCategory(name, categoryId)).thenReturn(shoe);

        Shoe foundShoe = shoeService.findByNameAndCategory(name, categoryId);

        assertNotNull(foundShoe);
        assertEquals(name, foundShoe.getShoeName());
        verify(shoeRepository, times(1)).findByNameAndCategory(name, categoryId);
    }

    @Test
    void findByNameAndCategory_NotFound() {
        String name = "NonExistent Shoe";
        Long categoryId = 999L;
        when(shoeRepository.findByNameAndCategory(name, categoryId)).thenReturn(null);

        assertThrows(ShoeNotFoundException.class, () -> {
            shoeService.findByNameAndCategory(name, categoryId);
        });
        verify(shoeRepository, times(1)).findByNameAndCategory(name, categoryId);
    }

    @Test
    void updateShoePrice_Success() {
        Long shoeId = 10L;
        double newPrice = 89.99;
        when(shoeRepository.existsById(shoeId)).thenReturn(true);
        when(shoeRepository.updateShoePrice(shoeId, newPrice)).thenReturn(1);

        int rowsAffected = shoeService.updateShoePrice(shoeId, newPrice);

        assertEquals(1, rowsAffected);
        verify(shoeRepository, times(1)).existsById(shoeId);
        verify(shoeRepository, times(1)).updateShoePrice(shoeId, newPrice);
    }

    @Test
    void updateShoePrice_ShoeNotFound() {
        Long shoeId = 999L;
        double newPrice = 79.99;
        when(shoeRepository.existsById(shoeId)).thenReturn(false);

        assertThrows(ShoeNotFoundException.class, () -> {
            shoeService.updateShoePrice(shoeId, newPrice);
        });
        verify(shoeRepository, times(1)).existsById(shoeId);
        verify(shoeRepository, never()).updateShoePrice(anyLong(), anyDouble());
    }

    @Test
    void getShoeNames_Success() throws NoShoesFoundException {
        List<String> names = List.of("Shoe1", "Shoe2");
        when(shoeRepository.getShoeNames()).thenReturn(names);

        List<String> result = shoeService.getShoeNames();

        assertEquals(2, result.size());
        assertEquals("Shoe1", result.get(0));
        verify(shoeRepository, times(1)).getShoeNames();
    }

    @Test
    void findShoesByPriceRange() {
        double minPrice = 50.0;
        double maxPrice = 150.0;
        List<Shoe> shoes = List.of(new Shoe("Shoe1", 42, 10, 60.0),
                new Shoe("Shoe2", 44, 5, 140.0));
        when(shoeRepository.findByPriceRange(minPrice, maxPrice)).thenReturn(shoes);

        List<Shoe> result = shoeService.findShoesByPriceRange(minPrice, maxPrice);

        assertEquals(2, result.size());
        verify(shoeRepository, times(1)).findByPriceRange(minPrice, maxPrice);
    }

    @Test
    void deleteShoe() {
        Long shoeId = 123L;

        shoeService.deleteShoe(shoeId);

        verify(shoeRepository, times(1)).deleteById(shoeId);
    }
}
