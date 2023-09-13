package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ProductFeedback;
import com.ecommerce.library.repository.ProductFeedbackRepository;
import com.ecommerce.library.service.ProductFeedbackService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductFeedbackImpl implements ProductFeedbackService {

    @Autowired
    private ProductFeedbackRepository productFeedbackRepository;
    @Autowired
    private ProductService productService;

    @Override
    public ProductFeedback saveReview(String review,Long id) {
        ProductFeedback feedback=new ProductFeedback();
        Product product=productService.findById(id);

        feedback.setReview(review);
        feedback.setProduct(product);
        return productFeedbackRepository.save(feedback);
    }

    @Override
    public ProductDto getProductFeedback(Long id) {
        Product products=productService.findById(id);
        List<ProductFeedback> feedbacks=products.getProductFeedbacks();
        ProductDto productDto=new ProductDto();
        List<ProductFeedback> review=new ArrayList<>();
        for(ProductFeedback feedback: feedbacks){
            review.add(feedback);
        }
        productDto.setFeedbacks(review);
        return  productDto;
    }
}
