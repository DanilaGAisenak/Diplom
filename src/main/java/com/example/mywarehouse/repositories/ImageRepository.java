package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Image;
import com.example.mywarehouse.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {}
