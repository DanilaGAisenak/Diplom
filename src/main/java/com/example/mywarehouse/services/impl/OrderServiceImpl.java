package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.Order;
import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.repositories.CompanyRepository;
import com.example.mywarehouse.repositories.OrderRepository;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.repositories.productRepository;
import com.example.mywarehouse.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final productRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public void saveOrder(Order order, String prodName, String comFromName, String comToName, Principal principal) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        Product product = productRepository.findProductByName(prodName);
        order.setProduct(product);
        Company companyFrom = companyRepository.findCompanyByName(comFromName);
        order.setCompanyFrom(companyFrom);
        Company companyTo = companyRepository.findCompanyByName(comToName);
        order.setCompanyTo(companyTo);
        User user = getUserByPrincipal(principal);
        if (user.getMasterId() != null) {
            order.setUser(userRepository.findByMasterId(user.getMasterId()));
        }
        else order.setUser(user);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrder(Integer id, Integer amount, Float sum, String comFromName, String comToName, String prodName) {
        Order order = orderRepository.findByOrderId(id);
        Company companyFr = companyRepository.findCompanyByName(comFromName);
        Company companyTo = companyRepository.findCompanyByName(comToName);
        Product product = productRepository.findProductByName(prodName);
        if (order != null) {
            order.setAmount(amount);
            order.setSum(sum);
            order.setProduct(product);
            order.setCompanyFrom(companyFr);
            order.setCompanyTo(companyTo);
        }
        orderRepository.save(order);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }
}
