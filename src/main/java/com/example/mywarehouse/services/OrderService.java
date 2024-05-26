package com.example.mywarehouse.services;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.Order;
import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;

import java.security.Principal;

public interface OrderService {
    void saveOrder(Integer amount, String prodName, String comFromName, String comToName, Principal principal);
    void deleteOrder(Integer id);
    void updateOrder(Integer id, Integer amount, String comFromName, String comToName, String prodName, Principal principal);
    User getUserByPrincipal(Principal principal);
}
