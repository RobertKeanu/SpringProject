package org.store.springproject.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.store.springproject.model.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    @Query("SELECT s.category FROM Shoe s WHERE s.id = :shoeId")
    Optional<Category> findById(Long shoeId);
}
