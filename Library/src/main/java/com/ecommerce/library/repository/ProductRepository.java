package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE p.is_deleted = false AND p.is_activated = true ORDER BY FUNCTION('RANDOM') LIMIT 4")
    List<Product> listViewProduct();

}
