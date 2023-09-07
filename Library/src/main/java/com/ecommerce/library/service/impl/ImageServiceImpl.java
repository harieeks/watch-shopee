package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Image;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ImageRepository;
import com.ecommerce.library.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;
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
}
