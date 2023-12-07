package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.enums.Role;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("users", userService.allUsers());
        return "usersMenu";
    }

    @GetMapping("/user/update/{user}")
    public String userInfo(@PathVariable("user") User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }


    @PostMapping("/user/update/{id}")
    public String saveUser(@PathVariable("id") Integer id, @RequestParam String username, @RequestParam String name,
                           @RequestParam String[] role /*@RequestParam MultipartFile file*/) throws IOException {
        User user = userRepository.findByUserId(id);
        userService.updateUser(user,username, name, role);
        return "redirect:/admin";
    }

    @PostMapping("/user/ban/{id}")
    public String userBan(@PathVariable("id") Integer id){
        userService.banUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteU(@PathVariable("id") Integer id){
        userRepository.deleteById(id);
        return "redirect:/admin";
    }


}
