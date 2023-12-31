package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;
    private String houseName;
    private String city;
    private String district;
    private String state;
    private String country;
    private Long pinCode;


}
