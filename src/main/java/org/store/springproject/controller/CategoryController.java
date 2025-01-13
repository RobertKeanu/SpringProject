package org.store.springproject.controller;

import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.store.springproject.dto.CategoryDto;
import org.store.springproject.model.Category;
import org.store.springproject.model.Shoe;
import org.store.springproject.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors.*;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/create-category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = new Category(categoryDto.getName());
        if (categoryDto.getShoes() != null) {
            List<Shoe> shoes = categoryDto.getShoes().stream()
                    .map(shoeDto -> {
                        Shoe shoe = new Shoe();
                        shoe.setShoeName(shoeDto.getName());
                        shoe.setPrice(shoeDto.getPrice());
                        shoe.setCategory(category);
                        return shoe;
                    })
                    .collect(Collectors.toList());
            category.setShoes(shoes);
        }
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryDto(savedCategory.getName(), null));
    }


}
