package org.store.springproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.springproject.model.Category;
import org.store.springproject.model.Shoe;
import org.store.springproject.repository.CategoryRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
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
    public Category createCategory(Category category) {
       //Category category = new Category(categoryName);
        log.info("Saving the category with name {}", category);
        return categoryRepository.save(category);
    }
}
