package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    List<Company> findByName(String name);

    List<Company> findAllByUser(User user);
}
