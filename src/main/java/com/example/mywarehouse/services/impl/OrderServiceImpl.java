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
    public void saveOrder(Integer amount, String prodName, String comFromName, String comToName, Principal principal) {
        Order order = new Order();
        if (getUserByPrincipal(principal).getMasterId() != null) {
            order.setUser(userRepository.findByUserId(getUserByPrincipal(principal).getMasterId()));
        }
        else order.setUser(getUserByPrincipal(principal));
        //List<Order> orderList = new ArrayList<>();
        //orderList.add(order);
        //Product product = productRepository.findProductByName(prodName);
        List<Product> whs = productRepository.findAllByUser(order.getUser());
        Product product = new Product();
        for (Product whouse: whs ) {
            if (whouse.getName().equals(prodName)) product = whouse;
        }
        order.setProduct(product);
        order.setAmount(amount);
        order.setSum(amount * product.getPrice());
        Company companyFrom = companyRepository.findCompanyByName(comFromName);
        order.setCompanyFrom(companyFrom);
        Company companyTo = companyRepository.findCompanyByName(comToName);
        order.setCompanyTo(companyTo);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrder(Integer id, Integer amount, String comFromName, String comToName, String prodName, Principal principal) {
        Order order = orderRepository.findByOrderId(id);
        Company companyFr = companyRepository.findCompanyByName(comFromName);
        Company companyTo = companyRepository.findCompanyByName(comToName);
        if (getUserByPrincipal(principal).getMasterId() != null) {
            order.setUser(userRepository.findByUserId(getUserByPrincipal(principal).getMasterId()));
        }
        else order.setUser(getUserByPrincipal(principal));
        List<Product> whs = productRepository.findAllByUser(order.getUser());
        Product product = new Product();
        for (Product whouse: whs ) {
            if (whouse.getName().equals(prodName)) product = whouse;
        }
        if (order != null) {
            order.setAmount(amount);
            order.setSum(amount * product.getPrice());
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
