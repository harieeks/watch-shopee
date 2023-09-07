package com.ecommerce.library.service;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import org.springframework.stereotype.Service;

public interface ShoppingCartService {

    ShoppingCart addItemsToCart(Product product, int quantity, Customer customer);
    ShoppingCart updateItemsInCart(Product product,int quantity,Customer customer);
    ShoppingCart deleteItemFromCart(Product product,Customer customer);

}
