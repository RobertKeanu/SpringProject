package org.store.springproject.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.springproject.dto.ShoeDto;
import org.store.springproject.exception.NoShoesFoundException;
import org.store.springproject.exception.ShoeAlreadyExistsException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.model.Shoe;
import org.store.springproject.service.OrderService;
import org.store.springproject.service.ReviewService;
import org.store.springproject.service.ShoeService;

import java.util.List;

@RequiredArgsConstructor
@RestController

public class ShoeController {
    private final ShoeService shoeService;
    private final ReviewService reviewService;
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(ShoeController.class);
    @PostMapping("/create_shoe")
    public ResponseEntity<Void> createShoe(@RequestBody ShoeDto shoeDto) {
        log.info("Creating shoe with name {} and size {}", shoeDto.getName(), shoeDto.getSize());
        try{
            shoeService.addShoe(shoeDto.getName(), shoeDto.getSize(), shoeDto.getStock(), shoeDto.getPrice());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ShoeAlreadyExistsException e)
        {
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/{shoeId}/delete_shoe", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteShoe(@PathVariable Long shoeId) throws ShoeNotFoundException {
        log.info("Deleting shoe with id {}", shoeId);
        shoeService.deleteShoe(shoeId);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/all_shoes", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllShoes() throws NoShoesFoundException {
        List<String> shoes;
        shoes = shoeService.getShoeNames();
        return ResponseEntity.ok(shoes);
    }
    @RequestMapping(value = "/{shoeId}/update_price", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateShoePrice(@PathVariable Long shoeId, @RequestParam double newPrice) {
        try{
            shoeService.updateShoePrice(shoeId, newPrice);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        catch (ShoeNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(value = "/get_shoes_range", method = RequestMethod.GET)
    public ResponseEntity<List<Shoe>> getShoesRange(@RequestParam double minPrice, @RequestParam double maxPrice) throws NoShoesFoundException {
        List<Shoe> shoes;
        shoes = shoeService.findShoesByPriceRange(minPrice, maxPrice);
        log.info("Showing shoes in the price interval");
        return ResponseEntity.ok(shoes);
    }
}
