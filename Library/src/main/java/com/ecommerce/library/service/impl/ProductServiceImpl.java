package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUpload imageUpload;
    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public List<ProductDto> listViewProduct() {
        List<Product> products=productRepository.listViewProduct();
        List<ProductDto> productDtoListView=transfer(products);
        return productDtoListView;
    }

    private List<ProductDto> transfer(List<Product> products) {
        List<ProductDto> productDtoList=new ArrayList<>();
        for (Product product:products){
            ProductDto productDto=new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCategory(product.getCategory());
            productDto.setImage(product.getImage());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.set_activated(product.is_activated());
            productDto.set_deleted(product.is_deleted());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto){
        try {
            Product product=new Product();

            if(imageProduct==null){
                product.setImage(null);
            }else {
                if(imageUpload.upload(imageProduct)){
                  // String dbimage=imageUpload.uploadImage(imageProduct);
                    product.setImage(imageProduct.getOriginalFilename());
                    System.out.println("ADDED SUCCESS FULLY"+product.getImage());
                }
//                imageUpload.upload(imageProduct);

            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);

            System.out.println("ADDED SUCCESS FULLY");
            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        Product product=productRepository.getById(productDto.getId());
        try {
            if(imageProduct==null){
                product.setImage(null);
            }else {
              if(imageUpload.checkExisted(imageProduct)==false) {
                  imageUpload.upload(imageProduct);
                  // String dbimage=imageUpload.uploadImage(imageProduct);
                  product.setImage(imageProduct.getOriginalFilename());
                  System.out.println("ImageUploaded");
              }
                  System.out.println("ImageExisted");
            }
            product.setImage(imageProduct.getOriginalFilename());
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());

            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product=productRepository.getById(id);
        product.setCategory(null);
        productRepository.save(product);
        productRepository.delete(product);
    }

    @Override
    public void disableById(Long id) {
        Product product=productRepository.getById(id);
        product.set_activated(false);
        product.set_deleted(true);
        System.out.println(product.is_activated()+"DISABLED");
        System.out.println(product.is_deleted()+"DISABLED");
        productRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product=productRepository.getById(id);
        product.set_deleted(false);
        product.set_activated(true);
        System.out.println(product.is_activated()+"ENABLED");
        System.out.println(product.is_deleted()+"ENABLED");
        productRepository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        Product product=productRepository.getById(id);
        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setImage(product.getImage());
        productDto.set_activated(product.is_activated());
        productDto.set_deleted(product.is_deleted());
        return productDto;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.getById(id);
    }
}
