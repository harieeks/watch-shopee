package com.ecommerce.library.repository;

import com.ecommerce.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Order findOrderById(Long id);

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllByOrderByOrderDate();
    @Query("SELECT MONTH(o.orderDate) AS month, COUNT(o) AS orderCount FROM Order o WHERE o.orderStatus <> 'Canceled' GROUP BY MONTH(o.orderDate)")
    List<Object[]> countOrdersByMonth();
    @Query("SELECT MONTH(o.orderDate) AS month, SUM(o.totalPrice) AS totalSum FROM Order o WHERE o.orderStatus <> 'Canceled' GROUP BY MONTH(o.orderDate)")
    List<Object[]> sumTotalPriceByMonth();
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderStatus <> 'Canceled'")
    Double getTotalPriceSum();
    @Query("SELECT DAY(o.orderDate) AS day, COUNT(o) AS orderCount FROM Order o WHERE o.orderStatus <> 'Canceled' GROUP BY DAY(o.orderDate)")
    List<Object[]> countOrdersByDay();
    @Query("SELECT MONTH(o.orderDate) AS month, COUNT(o) AS orderCount FROM Order o WHERE o.orderStatus = 'Canceled' GROUP BY MONTH(o.orderDate)")
    List<Object[]> cancelOrdersByMonth();




}
