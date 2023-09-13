package com.ecommerce.library.service;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.ProductFeedback;

import java.util.List;

public interface ProductFeedbackService {
    ProductFeedback saveReview(String review,Long id);
    ProductDto getProductFeedback(Long id);
}
