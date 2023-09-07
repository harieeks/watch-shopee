package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
    private Boolean is_deleted;
    private Boolean is_activated;

    public Category(String name){
        this.name=name;
        this.is_activated=true;
        this.is_deleted=false;
    }
}
