package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.model.WishlistItem;
import com.ecommerce.library.repository.WishlistItemRepository;
import com.ecommerce.library.repository.WishlistRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private WishlistItemRepository wishlistItemRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
    public Wishlist addToWishlist(String username, Long productId) {

        Customer customer=customerService.findByUsername(username);
        Product product=productService.findById(productId);

        Wishlist wishlist=customer.getWishlist();
        if(wishlist == null){
            wishlist=new Wishlist();
        }
        Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
        if(wishlistItems == null){
            wishlistItems=new HashSet<>();
            WishlistItem item=new WishlistItem();
            item.setProduct(product);
            item.setWishlist(wishlist);
            wishlistItems.add(item);
            wishlistItemRepository.save(item);
        }else{
            WishlistItem item=new WishlistItem();
            item.setProduct(product);
            item.setWishlist(wishlist);
            wishlistItems.add(item);
            wishlistItemRepository.save(item);
        }
        wishlist.setWishlistItems(wishlistItems);
        wishlist.setCustomer(customer);

        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist findCustomerWishlist(String username) {
        Customer customer=customerService.findByUsername(username);
         return wishlistRepository.findByCustomerId(customer.getId());
    }

    @Override
    public void removeWishlistItem(String username,Long productId) {
        Customer customer=customerService.findByUsername(username);
        Wishlist wishlist=customer.getWishlist();
        Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();

        WishlistItem item=findWishlistItem(wishlistItems,productId);
        if(item != null){
            wishlistItems.remove(item);
            wishlistItemRepository.delete(item);
        }
        wishlist.setWishlistItems(wishlistItems);
        wishlistRepository.save(wishlist);
    }

    private WishlistItem findWishlistItem(Set<WishlistItem> wishlistItems, Long productId) {
        if(wishlistItems == null){
            return null;
        }
        for(WishlistItem wishlistItem:wishlistItems){
            if(wishlistItem.getProduct().getId() == productId){
                return wishlistItem;
            }
        }
        return null;
    }
}
