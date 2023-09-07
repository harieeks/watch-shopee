package com.ecommerce.library.Dto;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewDto {

    private Long id;
    private String name;
    private String description;
    private double costPrice;
    private double salePrice;
    private int currentQuantity;
    private Category category;
    private boolean is_deleted;
    private boolean is_activated;
    private List<Image> images;
}
