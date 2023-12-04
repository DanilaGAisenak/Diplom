package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface productRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
}
