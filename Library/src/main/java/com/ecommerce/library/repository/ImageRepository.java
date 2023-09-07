package com.ecommerce.library.repository;

import com.ecommerce.library.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findImageByProductId(Long id);
}
