package com.example.mywarehouse.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="product")
//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer product_id;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "category")
    private String category;
    @Column(name = "price")
    private Float price;
    @Column(name = "img_link")
    private Integer img_link;
    @Column(name = "tax")
    private Integer tax;
    @Column(name="production_price")
    private Float production_price;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> images = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private Warehouse warehouse;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
    private List<Order> order = new ArrayList<>();
    private LocalDateTime dateOfCreate;

    @PrePersist
    private void init(){
        dateOfCreate = LocalDateTime.now();
    }

    public void addImage(Image image){
        image.setProduct(this);
        images.add(image);
    }

    public Integer getId(){
        return product_id;
    }
}
