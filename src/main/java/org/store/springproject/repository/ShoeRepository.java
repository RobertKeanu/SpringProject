package org.store.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.store.springproject.model.Shoe;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoeRepository extends JpaRepository<Shoe, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shoe s WHERE s.shoeName = :shoeName AND s.category.id = :categoryId")
    Shoe findByNameAndCategory(String shoeName, Long categoryId);

    @Transactional(readOnly = true)
    @Query("SELECT s.shoeName FROM Shoe s")
    List<String> getShoeNames();

    @Modifying
    @Transactional
    @Query("UPDATE Shoe s SET s.price = :price WHERE s.id = :id")
    int updateShoePrice(Long id, double price);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shoe s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    List<Shoe> findByPriceRange(double minPrice, double maxPrice);

    @Query("select s from Shoe s where s.shoeName = :shoeName")
    Optional<Shoe> findByShoeName(String shoeName);

}
