package com.ecommerce.library.service;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Image;
import com.ecommerce.library.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Image saveImage(MultipartFile imageProduct);
    List<Image> findImageByProductId(Long id);
    ProductDto productImages(Long id);
}
