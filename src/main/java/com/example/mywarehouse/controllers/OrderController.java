package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.*;
import com.example.mywarehouse.repositories.CompanyRepository;
import com.example.mywarehouse.repositories.OrderRepository;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.repositories.productRepository;
import com.example.mywarehouse.services.impl.CompanyServiceImpl;
import com.example.mywarehouse.services.impl.OrderServiceImpl;
import com.example.mywarehouse.services.impl.ProductServiceImpl;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final ProductServiceImpl productService;
    private final productRepository productRepository;
    private final CompanyServiceImpl companyService;
    private final CompanyRepository companyRepository;

    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        User user = new User();
        if( userService.getUserByPrincipal(principal).getMasterId() != null ){
            user = userRepository.findByUserId(userService.getUserByPrincipal(principal).getMasterId());
        }
        else user  =userService.getUserByPrincipal(principal);
        List<Order> list = orderRepository.findAllByUser(user);
        model.addAttribute("user",user);
        model.addAttribute("orders", list);
        return "orders";
    }

    @GetMapping("/orders/add")
    public String addOrders(Principal principal, Model model) {
        User user = new User();
        if( userService.getUserByPrincipal(principal).getMasterId() != null ){
            user = userRepository.findByUserId(userService.getUserByPrincipal(principal).getMasterId());
        }
        else user  =userService.getUserByPrincipal(principal);
        List<Product> product = productRepository.findAllByUser(user);
        model.addAttribute("products",product);
        List<Company> comFr = companyRepository.findAllByUser(user);
        model.addAttribute("companyFrom",comFr);
        List<Company> ComTo = companyRepository.findAll();
        model.addAttribute("companyTo",ComTo);
        return "add/addOrders";
    }
    @GetMapping("/orders/update/{id}")
    public String updOrd(@PathVariable("id")Integer id, Principal principal, Model model){
        User user = new User();
        if( userService.getUserByPrincipal(principal).getMasterId() != null ){
            user = userRepository.findByUserId(userService.getUserByPrincipal(principal).getMasterId());
        }
        else user  =userService.getUserByPrincipal(principal);
        List<Product> product = productRepository.findAllByUser(user);
        model.addAttribute("products",product);
        List<Company> comFr = companyRepository.findAllByUser(user);
        model.addAttribute("companyFrom",comFr);
        List<Company> ComTo = companyRepository.findAll();
        model.addAttribute("companyTo",ComTo);
        model.addAttribute("order",orderRepository.findByOrderId(id));
        return "update/updateOrder";
    }
    @GetMapping("/orders/delete/{id}")
    public String delOrd(@PathVariable("id")Integer id){
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }


    @PostMapping("/orders/update/{id}")
    public String updateOrder(@PathVariable("id")Integer id, @RequestParam Integer amount, @RequestParam String prodName,
                              @RequestParam String comFromName, @RequestParam String comToName, Principal principal){
        //Product product = productRepository.findProductByName(prodName);
        //Float sum = amount * product.getPrice();
        orderService.updateOrder(id,amount, comFromName, comToName, prodName, principal);
        return "redirect:/orders";
    }
    @PostMapping("/orders/add")
    public String aadO(Principal principal, Order order, @RequestParam String prodName,
                       @RequestParam String comFromName, @RequestParam String comToName,
                       @RequestParam Integer amount){
            orderService.saveOrder(amount, prodName, comFromName, comToName, principal);
        return "redirect:/orders";
    }
}
