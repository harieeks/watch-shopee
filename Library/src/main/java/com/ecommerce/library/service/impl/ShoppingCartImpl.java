package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartImpl implements ShoppingCartService {

    @Autowired
    private CartItemRepository itemRepository;
    @Autowired
    private ShoppingCartRepository cartRepository;

    @Override
    public ShoppingCart addItemsToCart(Product product, int quantity, Customer customer) {

       //getting the customers shopping cart
        ShoppingCart cart=customer.getShoppingCart();
        if(cart == null){
            cart=new ShoppingCart();
        }
        //getting the cart items from the cart
        Set<CartItem> cartItems=cart.getCartItem();
        //check if there is same product in cart item
        CartItem cartItem=findCartItems(cartItems, product.getId());
        //if there is no items in the cart
        if(cartItems == null){
            cartItems=new HashSet<>();
            if(cartItem == null){
                //if there is no existing cart item related to the product creating a new cart item
                cartItem=new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(product.getCostPrice()*quantity);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }
        }else {
            //if the cart have items and there is no existing product relate to the selected product
            if(cartItem == null){
                cartItem=new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }else {
                //if the cart have existing products
                cartItem.setQuantity(cartItem.getQuantity()+quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice()+(quantity* product.getCostPrice()));
                itemRepository.save(cartItem);
            }

        }
        //adding the selected items to the cart
        cart.setCartItem(cartItems);
        //updating other fields in cart
        int totalItems=totalItems(cart.getCartItem());
        double totalPrice=totalPrice(cart.getCartItem());
        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
        cart.setCustomer(customer);

        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateItemsInCart(Product product, int quantity, Customer customer) {

        ShoppingCart cart=customer.getShoppingCart();
        Set<CartItem> cartItems=cart.getCartItem();

        CartItem item=findCartItems(cartItems,product.getId());
        item.setQuantity(quantity);
        item.setTotalPrice(quantity*product.getCostPrice());
        itemRepository.save(item);

        cart.setCartItem(cartItems);
        int totalItems=totalItems(cart.getCartItem());
        double totalPrice=totalPrice(cart.getCartItem());
        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteItemFromCart(Product product, Customer customer) {

        ShoppingCart cart=customer.getShoppingCart();
        Set<CartItem> cartItem=cart.getCartItem();

        CartItem item=findCartItems(cartItem,product.getId());
        cartItem.remove(item);

        itemRepository.delete(item);
        cart.setCartItem(cartItem);
        int totalItems=totalItems(cartItem);
        double totalPrice=totalPrice(cartItem);
        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);

        return cartRepository.save(cart);
    }

    private CartItem findCartItems(Set<CartItem> cartItems,Long productId){
        if(cartItems == null){
            return null;
        }
        CartItem cartItem=null;
        for(CartItem item: cartItems){
            if(item.getProduct().getId() == productId){
                cartItem=item;
            }
        }
        return cartItem;
    }

    private int totalItems(Set<CartItem> cartItems){
        int totalItems =0;
        for(CartItem item: cartItems){
            totalItems +=item.getQuantity();
        }
        return totalItems;
    }

    private double totalPrice(Set<CartItem> cartItems){
        double totalPrice=0;
        for(CartItem item : cartItems){
            totalPrice+=item.getTotalPrice();
        }
        return totalPrice;
    }
}
