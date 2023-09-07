package com.ecommerce.library.service;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;

public interface WishlistService {

    Wishlist addToWishlist(String username,Long productId);
    Wishlist findCustomerWishlist(String username);
    void removeWishlistItem(String username,Long productId);
}
