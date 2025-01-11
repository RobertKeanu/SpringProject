package org.store.springproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springproject.model.Category;
import org.store.springproject.model.Shoe;
import org.store.springproject.repository.CategoryRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ShoeService shoeService;
    Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    public Optional<Category> findByShoeId(Long shoeId){
        //Optional<Shoe> shoe = shoeService.findById(shoeId);
        return categoryRepository.findById(shoeId);
    }

}
