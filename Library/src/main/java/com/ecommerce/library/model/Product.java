package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products",uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String name;
    private String description;
    private double costPrice;
    private double salePrice;
    private int currentQuantity;

    @OneToMany(mappedBy = "product")
    private List<Image> image;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",referencedColumnName = "category_id")
    private Category category;
    private boolean is_deleted;
    private boolean is_activated;

    @OneToMany(mappedBy = "product")
    List<ProductFeedback> productFeedbacks;
}
