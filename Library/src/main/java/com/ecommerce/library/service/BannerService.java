package com.ecommerce.library.service;

import com.ecommerce.library.model.Banner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {

    Banner save(MultipartFile bannerImage,Banner banner);
    void deleteBanner(Long id);

    List<Banner> findAllBanners();
}
