package com.ecommerce.library.service;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<ProductDto> findAll();
    List<Product> findAllProduct();
    List<ProductDto> listViewProduct();
    Product save(List<MultipartFile> imageProduct, ProductDto productDto);
    Product update(List<MultipartFile> imageProduct,ProductDto productDto);
    void deleteById(Long id);
    void disableById(Long id);
    void enableById(Long id);
    ProductDto getById(Long id);
    Product findById(Long id);
    List<Map<String,Object>> salesReport();
    List<Product>listAnalog(String keyword);
    List<Product> getSmartWatch();
    List<Product> getDigitalWatch();
    List<Product> getAnalogWatch();
    List<Product>listDigital(String keyword);
    List<Product>listSmart(String keyword);
    Product getSingleAnalogProduct();
    Product getSingleDigitalProduct();
    Product getSingleSmartProduct();
    List<Product> findAnalogProductByPrice(double minPrice,double maxPrice);
    List<Product> findDigitalProductByPrice(double minPrice,double maxPrice);
    List<Product> findSmartProductByPrice(double minPrice,double maxPrice);
    Page<Product> allWatchPagination(int pageNo,int pageSize);



}
