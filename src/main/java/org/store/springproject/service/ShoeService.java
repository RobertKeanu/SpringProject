package org.store.springproject.service;

import lombok.RequiredArgsConstructor;
import org.store.springproject.exception.NoShoesFoundException;
import org.store.springproject.exception.ShoeAlreadyExistsException;
import org.store.springproject.model.Shoe;
import org.store.springproject.repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.store.springproject.exception.ShoeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShoeService {
    @Autowired
    private final ShoeRepository shoeRepository;
    public Shoe findByNameAndCategory(String name, Long categoryId) {
        Shoe shoe = shoeRepository.findByNameAndCategory(name, categoryId);
        if (shoe == null) {
            throw new ShoeNotFoundException("Shoe not found");
        }
        return shoe;
    }
    public List<String> getShoeNames() throws NoShoesFoundException {
        return shoeRepository.getShoeNames();
    }
    public int updateShoePrice(Long id, double price)
    {
        if(!shoeRepository.existsById(id)){
            throw new ShoeNotFoundException("Shoe not found");
        }
        return shoeRepository.updateShoePrice(id, price);
    }
    public List<Shoe> findShoesByPriceRange(double minPrice, double maxPrice) {
        return shoeRepository.findByPriceRange(minPrice, maxPrice);
    }

    public Optional<Shoe> findById(Long shoeId) {
        return shoeRepository.findById(shoeId);
    }

    public Shoe addShoe(String shoeName, int size, int stock, double price) {
        if(shoeRepository.findByShoeName(shoeName).isPresent()) {
            throw new ShoeAlreadyExistsException();
        }
        Shoe shoe = new Shoe(shoeName,size,stock,price);
        return shoeRepository.save(shoe);
    }
    public void deleteShoe(Long shoeId) {
        shoeRepository.deleteById(shoeId);
    }
}
