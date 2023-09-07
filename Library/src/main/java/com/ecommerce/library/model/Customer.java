package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers",uniqueConstraints = @UniqueConstraint(columnNames = {"username","phone_number"}))
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable( name = "customer_roles",
            joinColumns = @JoinColumn(name = "customer_id",referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "role_id"))
    private Collection<Role> roles;
    private String password;
    private boolean isDeleted;
    private boolean isEnabled;

    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER)
    private List<Address> addresses;

    @OneToOne(mappedBy = "customer")
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToOne(mappedBy = "customer")
    private Wishlist wishlist;

    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

}
