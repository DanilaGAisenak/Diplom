package com.example.mywarehouse.services;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.Order;
import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;

import java.security.Principal;

public interface OrderService {
    void saveOrder(Order order, String prodName, String comFromName, String comToName, Principal principal);
    void deleteOrder(Integer id);
    void updateOrder(Integer id, Integer amount, Float sum, String comFromName, String comToName, String prodName);
    User getUserByPrincipal(Principal principal);
}
