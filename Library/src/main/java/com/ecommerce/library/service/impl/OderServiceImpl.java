package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.AddressDto;
import com.ecommerce.library.Dto.MonthlyOrderCount;
import com.ecommerce.library.Dto.OrderDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.*;
import com.ecommerce.library.service.*;
import com.ecommerce.library.utils.MonthName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;


    @Override
    public Order save(ShoppingCart shoppingCart,String paymentMethod,Long id) {

        Order order=new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus("Confirmed");
        order.setCustomer(shoppingCart.getCustomer());
        order.setShippingFee((double) 0);
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod(paymentMethod);
        order.setDeliveyDate(new Date());
        Address address=addressService.getById(id);
        order.setAddress(address);


        List<OrderDetails> orderDetailsList=new ArrayList<>();
        for(CartItem item: shoppingCart.getCartItem()){
            OrderDetails orderDetails=new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setQuantity(item.getQuantity());
            orderDetails.setUnitPrice(item.getProduct().getCostPrice());
            orderDetails.setProduct(item.getProduct());
            orderDetailsRepository.save(orderDetails);
            orderDetailsList.add(orderDetails);
            cartItemRepository.delete(item);

        }

        for(CartItem item: shoppingCart.getCartItem()){
           Product product=productService.findById(item.getProduct().getId());
           product.setCurrentQuantity(product.getCurrentQuantity()-item.getQuantity());
           productRepository.save(product);
        }


        order.setOrderDetails(orderDetailsList);
        shoppingCart.setCartItem(new HashSet<>());
        shoppingCart.setTotalItems(0);
        shoppingCart.setTotalPrice(0);
        shoppingCartRepository.save(shoppingCart);
       return orderRepository.save(order);
    }

    @Override
    public List<OrderDto> getOrders(Customer customer) {
        List<Order> orderList=customer.getOrders();
        List<OrderDto> orderDtoList=new ArrayList<>();
        for(Order order:orderList){
            OrderDto orderDto=new OrderDto();

            orderDto.setCustomer(order.getCustomer());
            orderDto.setOrderDate(order.getOrderDate());
            orderDto.setDeliveyDate(order.getDeliveyDate());
            orderDto.setAddress(transferAddress(order.getAddress()));
            orderDto.setPaymentMethod(order.getPaymentMethod());
            orderDto.setShippingFee(order.getShippingFee());
            orderDto.setTotalPrice(order.getTotalPrice());
            orderDto.setOrderStatus(order.getOrderStatus());
            orderDto.setOrderDetails(order.getOrderDetails());
            orderDto.setAccept(order.isAccept());
            orderDto.setNotes(order.getNotes());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    public synchronized Order cancelOrder(Order order,String username) {
        order.setOrderStatus("Canceled");
        Customer customer=customerService.findByUsername(username);
        Wallet wallet=walletService.findCustomerWallet(customer.getId());


        List<OrderDetails> orderDetailsList = new ArrayList<>(order.getOrderDetails());

        for (OrderDetails productQty : orderDetailsList) {
            Product product = productService.findById(productQty.getProduct().getId());
            product.setCurrentQuantity(product.getCurrentQuantity() + productQty.getQuantity());
            productRepository.save(product);

            productQty.setQuantity(0);
            productQty.setUnitPrice(0);
            productQty.setTotalPrice(0);
            orderDetailsRepository.save(productQty);
        }
        if(order.getPaymentMethod().equals("razorPay") && wallet != null){
            wallet.setAmount(order.getTotalPrice());
            walletRepository.save(wallet);
        }
        order.setTotalPrice(0);
        return orderRepository.save(order);
    }


    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByOrderDate();
    }

    @Override
    public List<Map<String, Object>> getMonthlyEarnings() {

        List<Object[]> result=orderRepository.sumTotalPriceByMonth();
        List<Map<String,Object>> monthlyEarnings=new ArrayList<>();

        if(!result.isEmpty()){
            for(Object[] row : result){
                Map<String, Object> dataPoint = new HashMap<>();

                if(row[0] != null){
                    int month = (int) row[0];
                    String monthName= MonthName.getMonthName(month);
                    dataPoint.put("month", monthName);
                    System.out.println(dataPoint);
                }
                double totalEarnings = (double) row[1];
                dataPoint.put("totalEarnings", totalEarnings);

                monthlyEarnings.add(dataPoint);
            }
        }
        return monthlyEarnings;
    }

    @Override
    public List<Map<String, Object>> getMonthlyOrder() {

        List<Object[]> result=orderRepository.countOrdersByMonth();
        List<Map<String, Object>> monthlyOrderCounts = new ArrayList<>();

        if(!result.isEmpty()){
            System.out.println("result not empty");

            for(Object[] row : result){
                Map<String, Object> dataPoint = new HashMap<>();

                if(row[0] != null){
                    int month = (int) row[0];
                    String monthName= MonthName.getMonthName(month);
                    dataPoint.put("month", monthName);
                    System.out.println(dataPoint);
                }
                long orderCount = (long) row[1];
                dataPoint.put("orderCount", orderCount);

                monthlyOrderCounts.add(dataPoint);
            }
        }
        return monthlyOrderCounts;
    }

    @Override
    public Order updateOrderStatus(Long id, String status) {
        Order order=findOrderById(id);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public double sumofTotalPrice() {
        return orderRepository.getTotalPriceSum();
    }

    @Override
    public List<Map<String, Object>> getOrdersPerDay() {
        List<Object[]> result=orderRepository.countOrdersByDay();
        List<Map<String,Object>> dailyOrder=new ArrayList<>();

        for(Object[] row : result){
            Map<String, Object> dataPoint = new HashMap<>();

            if(row[0] != null){
                int day = (int) row[0];
                dataPoint.put("day", day);
                System.out.println(dataPoint);
            }
            long orderCount = (long) row[1];
            dataPoint.put("orderCount", orderCount);

            dailyOrder.add(dataPoint);
        }
        return dailyOrder;
    }

    @Override
    public List<Map<String,Object>> getMonthlyCancelReport() {
        List<Object[]> orders=orderRepository.cancelOrdersByMonth();
        List<Map<String,Object>> monthlyOrder=new ArrayList<>();



        for(Object[] row:orders){
            Map<String, Object> dataPoint = new HashMap<>();

            if(row[0] != null){
                int month = (int) row[0];
                String monthName= MonthName.getMonthName(month);
                dataPoint.put("month",monthName);
            }
            long count = (long) row[1];
            dataPoint.put("count",count);
            monthlyOrder.add(dataPoint);
        }
        System.out.println("inside the implimentation "+monthlyOrder);

        return monthlyOrder;
    }


    private AddressDto transferAddress(Address address){
        AddressDto addressDto=new AddressDto();
        addressDto.setHouseName(address.getHouseName());
        addressDto.setCity(address.getCity());
        addressDto.setDistrict(address.getDistrict());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        addressDto.setPinCode(address.getPinCode());
        return  addressDto;
    }
}
