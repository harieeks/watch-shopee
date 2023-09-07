package com.ecommerce.library.model;

import com.ecommerce.library.Dto.AddressDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private Date orderDate;
    private Date deliveyDate;
    private double totalPrice;
    private Double shippingFee;
    private String notes;
    private String orderStatus;
    private String paymentMethod;
    private boolean isAccept;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id",referencedColumnName = "address_id")
    private Address address;

//    private AddressDto addressDto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetails> orderDetails;
}
