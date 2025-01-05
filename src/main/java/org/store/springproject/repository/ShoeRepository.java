package org.store.springproject.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.store.springproject.model.Category;
import org.store.springproject.model.Shoe;

import java.util.List;

@Repository
public interface ShoeRepository extends CrudRepository<Shoe, Long> {
    @Transactional(readOnly = true)
    @Query("select s from Shoe s join Category ca on s.category.id = ca.id where s.shoeName = :shoeName and ca.id = :id")
    Shoe findByNameAndCategory(String name);

    @Transactional(readOnly = true)
    @Query("SELECT s.shoeName FROM Shoe s")
    List<String> getShoeNames();

    @Modifying
    @Query("update Shoe s set s.price = :price where s.id = :id")
    Shoe updateShoePrice(int id, double price);
}
