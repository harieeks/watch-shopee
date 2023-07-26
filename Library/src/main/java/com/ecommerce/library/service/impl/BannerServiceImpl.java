package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Banner;
import com.ecommerce.library.repository.BannerRepository;
import com.ecommerce.library.service.BannerService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private ImageUpload imageUpload;
    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Banner save(MultipartFile bannerImage,Banner banner) {
        try {
            if(bannerImage==null){
                throw new RuntimeException("No banner file");
            }
            if(imageUpload.bannerUpload(bannerImage)){
                banner.setImage(bannerImage.getOriginalFilename());
            }
           return bannerRepository.save(banner);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteBanner(Long id) {
        Banner banner=bannerRepository.getById(id);
        bannerRepository.delete(banner);
    }

    @Override
    public List<Banner> findAllBanners() {
        return bannerRepository.findAll();
    }
}
