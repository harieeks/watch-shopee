package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @Override
    public String toString() {
        return "ProductFeedback{" +
                "review='" + review + '\'' +
                '}';
    }

    private String review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id",referencedColumnName = "product_id")
    private Product product;
}
