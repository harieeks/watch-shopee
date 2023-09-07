package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE p.is_deleted = false AND p.is_activated = true ")
    List<Product> listViewProduct();
    @Query("SELECT p.name, p.currentQuantity FROM Product p WHERE p.is_deleted = false AND p.is_activated = true")
    List<Object[]> stockReport();
    @Query("SELECT p FROM Product p WHERE p.name ILIKE %?1% OR p.description ILIKE %?1% AND p.category.name = 'Analog Watch'")
    List<Product> findAnalogProduct(String keyword);
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Smart watch' AND p.is_activated = true")
    List<Product> findProductsByCategorySmartWatch();
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Digital Watch' AND p.is_activated = true")
    List<Product> findProductsByCategoryDigitalWatch();
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Analog Watch' AND p.is_activated = true")
    List<Product> findProductsByCategoryAnalogWatch();
    @Query("SELECT p FROM Product p WHERE p.name ILIKE %?1% OR p.description ILIKE %?1% AND p.category.name = 'Digital Watch'")
    List<Product> findDigitalProduct(String keyword);
    @Query("SELECT p FROM Product p WHERE p.name ILIKE %?1% OR p.description ILIKE %?1% AND p.category.name = 'Smart watch'")
    List<Product> findSmartProduct(String keyword);
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Smart watch' AND p.is_activated = true ORDER BY FUNCTION('RANDOM') LIMIT 1")
    Product findSingleSmartProduct();
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Digital Watch' AND p.is_activated = true ORDER BY FUNCTION('RANDOM') LIMIT 1")
    Product findSingleDigitalProduct();
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Analog Watch' AND p.is_activated = true ORDER BY FUNCTION('RANDOM') LIMIT 1")
    Product findSingleAnalogProduct();
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Analog Watch' AND p.is_deleted = false AND p.costPrice >= ?1 AND p.costPrice <= ?2")
    List<Product> findAnalogProductByPriceRange(double minPrice,double maxPrice);
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Digital Watch' AND p.is_deleted = false AND p.costPrice >= ?1 AND p.costPrice <= ?2")
    List<Product> findDigitalProductByPriceRange(double minPrice,double maxPrice);
    @Query("SELECT p FROM Product p WHERE p.category.name = 'Smart watch' AND p.is_deleted = false AND p.costPrice >= ?1 AND p.costPrice <= ?2")
    List<Product> findSmartProductByPriceRange(double minPrice,double maxPrice);

}
