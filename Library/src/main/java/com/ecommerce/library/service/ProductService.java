package com.ecommerce.library.service;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductDto> findAll();
    List<ProductDto> listViewProduct();
    Product save(MultipartFile imageProduct, ProductDto productDto);
    Product update(MultipartFile imageProduct,ProductDto productDto);
    void deleteById(Long id);
    void disableById(Long id);
    void enableById(Long id);
    ProductDto getById(Long id);
    Product findById(Long id);
}
