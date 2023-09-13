package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {

    String rootPath = System.getProperty("user.home");

    String UPLOAD_FOLDER = rootPath +  File.separator +"Downloads"+ File.separator + "Ecommerce-watchshopee" + File.separator + "Admin" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "Image-product";
    String customer_product = rootPath + File.separator +"Downloads"+  File.separator + "Ecommerce-watchshopee" + File.separator + "Customer" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "image-product";

    private final String banner_admin="C:\\Users\\hp\\Downloads\\Ecommerce-watchshopee\\Admin\\src\\main\\resources\\static\\images\\Banner-image\\";
    private final String banner_customer="C:\\Users\\hp\\Downloads\\Ecommerce-watchshopee\\Customer\\src\\main\\resources\\static\\images\\Banner-image\\";

    public boolean upload(MultipartFile imageProduct){
        boolean isUpload=false;
        try {
            Files.copy(imageProduct.getInputStream(),
                    Paths.get(UPLOAD_FOLDER+File.separator,
                            imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy(imageProduct.getInputStream(),
                    Paths.get(customer_product+File.separator,
                            imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return isUpload;
    }

    public boolean bannerUpload(MultipartFile bannerImage){
        boolean isBannerUpload=false;
        try {
            Files.copy(bannerImage.getInputStream(),
                    Paths.get(banner_admin+File.separator,
                            bannerImage.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy(bannerImage.getInputStream(),
                    Paths.get(banner_customer+File.separator,
                            bannerImage.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);

            isBannerUpload=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return isBannerUpload;
    }

    public boolean checkBannerExist(MultipartFile bannerImage){
        boolean isBannerExist=false;
        try {
            File file=new File(bannerImage.getOriginalFilename());
            isBannerExist= file.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isBannerExist;
    }

    public boolean checkExisted(MultipartFile imageProduct){
        boolean isExisted=false;
        try{
            File file=new File(imageProduct.getOriginalFilename());
            isExisted= file.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isExisted;
    }


}
