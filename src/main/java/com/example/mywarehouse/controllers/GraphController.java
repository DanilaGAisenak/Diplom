package com.example.mywarehouse.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.mywarehouse.excel.Write;
import com.example.mywarehouse.models.Order;
import com.example.mywarehouse.repositories.OrderRepository;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GraphController {
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;

    @GetMapping("/displayBarGraph")
    public String barGraph(Model model, Principal principal) {
        List<Order> orders = orderRepository.findAllByUser(userService.getUserByPrincipal(principal));
        float max = 0;
        Map<String, Float> surveyMap = new LinkedHashMap<>();
        for (Order order: orders) {
            surveyMap.put(order.getCompanyTo().getName(), order.getSum());
            max = order.getSum() > max? order.getSum() : max;
        }
        model.addAttribute("surveyMap", surveyMap);
        model.addAttribute("max", max);
        return "barGraph";
    }

    @GetMapping("/displayPieChart")
    public String pieChart(Model model) {
//        model.addAttribute("pass", 50);
//        model.addAttribute("fail", 50);
//        return "pieChart";
        model.addAttribute("user", userService.countUserByRoleUser());
        model.addAttribute("admin", userService.countUserByRoleAdmin());
        model.addAttribute("moder", userService.countUserByRoleModer());
        return "pieChart";
    }

    @GetMapping("/saveBill")
    public String saveBill(Principal principal){
        List<Order> orders = orderRepository.findAllByUser(userService.getUserByPrincipal(principal));
        Write.write(orders);
        return "redirect:/";
    }

}
