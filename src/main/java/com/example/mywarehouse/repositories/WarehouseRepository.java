package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    List<Warehouse> findByName(String name);

    List<Warehouse> findAllByUser(User user);
    Warehouse findByWarehouseId(Integer id);
    Warehouse findWarehouseByName(String name);
}
