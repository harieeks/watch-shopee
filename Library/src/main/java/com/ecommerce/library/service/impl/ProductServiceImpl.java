package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Image;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ImageRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ImageService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import com.ecommerce.library.utils.MonthName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUpload imageUpload;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public List<Product> findAllProduct() {
        List<Product> products=productRepository.findAll();
        return products ;
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
           // productDto.setImage(product.getImage());
            productDto.setImages(product.getImage());
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
    public Product save(List<MultipartFile> imageProduct, ProductDto productDto){
        try {
            Product product=new Product();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            product.setImage(null);

            Product productNew=productRepository.save(product);
            //Long id=product1.getId();
            //Product productNew=findById(id);
            System.out.println(productNew);

            List<Image> imageList=new ArrayList<>();

            for(MultipartFile imageFile : imageProduct){
                Image image= new Image();

                image.setImagePath(imageFile.getOriginalFilename());
                image.setProduct(productNew);
                imageRepository.save(image);
                imageList.add(image);
                imageUpload.upload(imageFile);
            }
            productNew.setImage(imageList);

            System.out.println("ADDED SUCCESS FULLY");
            return productRepository.save(productNew);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(List<MultipartFile> imageProduct, ProductDto productDto) {
        Product product=productRepository.getById(productDto.getId());
        try {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);

            Product newProduct= productRepository.save(product);

            List<Image> imageList=product.getImage();
            if(imageProduct==null){
//                newProduct.setImage(null);
            }else{
                for(MultipartFile file:imageProduct){
                    if(imageUpload.checkExisted(file)==false){
                        imageUpload.upload(file);
                        System.out.println("image uploaded");
                    }
                    Image image=new Image();
                    image.setImagePath(file.getOriginalFilename());
                    image.setProduct(newProduct);
                    System.out.println("image setting");
                    if(!imageList.contains(image)){
                        imageList.add(image);
                        imageRepository.save(image);
                        System.out.println("inside the if");
                    }
                }
                newProduct.setImage(imageList);
            }
            return productRepository.save(newProduct);
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
        productDto.setImages(product.getImage());
        productDto.set_activated(product.is_activated());
        productDto.set_deleted(product.is_deleted());
        return productDto;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public List<Map<String, Object>> salesReport() {
        List<Object[]> product=productRepository.stockReport();
        List<Map<String,Object>> stock=new ArrayList<>();

        for(Object[] row:product){
            Map<String, Object> dataPoint = new HashMap<>();

            if(row[0] != null){
                String productName = (String) row[0];
                dataPoint.put("productName",productName);
            }
            int productQuantity = (int) row[1];
            dataPoint.put("productQuantity",productQuantity);
            stock.add(dataPoint);
        }
        System.out.println("inside the implimentation "+stock);

        return stock;
    }

    @Override
    public List<Product> listAnalog(String keyword) {
        if(keyword.isEmpty()){
            return productRepository.findProductsByCategoryAnalogWatch();
        }
        List<Product> products= productRepository.findAnalogProduct(keyword);
        return products;
    }

    @Override
    public List<Product> getSmartWatch() {
        return productRepository.findProductsByCategorySmartWatch();
    }

    @Override
    public List<Product> getDigitalWatch() {
        return productRepository.findProductsByCategoryDigitalWatch();
    }

    @Override
    public List<Product> getAnalogWatch() {
        List<Product> product=productRepository.findProductsByCategoryAnalogWatch();
        return productRepository.findProductsByCategoryAnalogWatch();
    }

    @Override
    public List<Product> listDigital(String keyword) {
        if(keyword.isEmpty()){
            return productRepository.findProductsByCategoryDigitalWatch();
        }
        List<Product> products= productRepository.findDigitalProduct(keyword);
        return products;
    }

    @Override
    public List<Product> listSmart(String keyword) {
        if(keyword.isEmpty()){
            return productRepository.findProductsByCategorySmartWatch();
        }
        List<Product> products= productRepository.findSmartProduct(keyword);
        return products;
    }

    @Override
    public Product getSingleAnalogProduct() {
        return productRepository.findSingleAnalogProduct();
    }

    @Override
    public Product getSingleDigitalProduct() {
        return productRepository.findSingleDigitalProduct();
    }

    @Override
    public Product getSingleSmartProduct() {
        return productRepository.findSingleSmartProduct();
    }

    @Override
    public List<Product> findAnalogProductByPrice(double minPrice,double maxPrice) {
        return productRepository.findAnalogProductByPriceRange(minPrice,maxPrice);
    }

    @Override
    public List<Product> findDigitalProductByPrice(double minPrice, double maxPrice) {
        return productRepository.findDigitalProductByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> findSmartProductByPrice(double minPrice, double maxPrice) {
        return productRepository.findSmartProductByPriceRange(minPrice, maxPrice);
    }

    @Override
    public Page<Product> allWatchPagination(int pageNo, int pageSize) {
        Pageable pageable= PageRequest.of(pageNo-1,pageSize);
        return productRepository.findAll(pageable);
    }
}
