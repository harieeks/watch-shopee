package com.ecommerce.library.repository;

import com.ecommerce.library.model.ProductFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFeedbackRepository extends JpaRepository<ProductFeedback,Long> {
}
