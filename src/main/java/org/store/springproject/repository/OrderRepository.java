package org.store.springproject.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.store.springproject.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.price > :minTotalPrice")
    List<Order> findByMinTotalPrice(@Param("minTotalPrice") double minTotalPrice);

    @Query("SELECT o FROM Order o JOIN o.shoes s WHERE s.id = :shoeId AND o.paymentMethod.id = :paymentMethodId")
    List<Order> findByShoeIdAndPaymentId(@Param("shoeId") Long shoeId, @Param("paymentId") Long paymentId);
}
