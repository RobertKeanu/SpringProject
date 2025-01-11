package org.store.springproject.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.store.springproject.model.PaymentMethod;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
    @Query("select pm from PaymentMethod pm where pm.method = :method")
    List<PaymentMethod> findByType(String method);

    //nu cred ca merge
    @Query("select distinct pm from PaymentMethod pm join Order o on pm.id = o.id where o.price > :minTotalPrice")
    List<PaymentMethod> findPaymentMethodsByMinOrderTotalPrice(@Param("minTotalPrice") double minTotalPrice);
}
