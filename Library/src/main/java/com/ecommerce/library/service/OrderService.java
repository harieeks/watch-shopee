package com.ecommerce.library.service;

import com.ecommerce.library.Dto.OrderDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;
import java.util.Map;

public interface OrderService {

     Order save(ShoppingCart shoppingCart,String paymentMethod,Long id);
     List<OrderDto> getOrders(Customer customer);
     Order findOrderById(Long id);
     Order cancelOrder(Order order,String username);
     List<Order> findAllOrders();
     List<Map<String,Object>> getMonthlyEarnings();
     List<Map<String, Object>> getMonthlyOrder();
     Order updateOrderStatus(Long id,String status);
     double sumofTotalPrice();
     List<Map<String,Object>> getOrdersPerDay();
     List<Map<String,Object>> getMonthlyCancelReport();


}
