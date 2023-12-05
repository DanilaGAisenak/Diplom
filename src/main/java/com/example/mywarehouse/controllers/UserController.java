package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.User;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @GetMapping("/")
    public String securityUrl(Principal principal, Model model){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        return "hello";
    }

    @PostMapping("/")
    public String hi(Principal principal, Model model){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        return "hello";
    }

//    @PostMapping("/login")
//    public String log(){
//        return "redirect:/";
//    }

    @PostMapping("/registration")
    public String CreateUser(User user, Model model){
        if (!userService.createUser(user)){
            model.addAttribute("errorMessage", "Пользователь с username: "+user.getUsername()+"уже существует");
            return "registration";
        }
        //userService.createUser(user);
        return "redirect:/login";
    }
}
