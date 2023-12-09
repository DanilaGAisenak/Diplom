package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Order;
import com.example.mywarehouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderId(Integer id);
    List<Order> findAllByUser(User user);
}
