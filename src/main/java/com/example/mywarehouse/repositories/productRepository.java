package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface productRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
    List<Product> findAllByUser(User user);
    Product findProductByName(String name);
    Product findProductByUser(User user);
}
