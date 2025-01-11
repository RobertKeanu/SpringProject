package org.store.springproject.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.store.springproject.model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByUser_Id(Integer userId);

    @Query("select r from Review r where r.review >= :minRating")
    List<Review> findByMinRating(@Param("minRating") int minRating);
}
