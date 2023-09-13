package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Image;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ImageRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ImageService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Image saveImage(MultipartFile imageProduct) {
        Image image=new Image();
        image.setImagePath(imageProduct.getOriginalFilename());
        return image;
    }

    @Override
    public List<Image> findImageByProductId(Long id) {
        return imageRepository.findImageByProductId(id);
    }

    @Override
    public ProductDto productImages(Long id) {
        Product productImage=productRepository.getById(id);
        List<Image> images=productImage.getImage();
        ProductDto productDto=new ProductDto();
        List<Image> imageList=new ArrayList<>();
        for(Image image :images){
            imageList.add(image);
        }
        productDto.setImages(images);
        return productDto;
    }
}
