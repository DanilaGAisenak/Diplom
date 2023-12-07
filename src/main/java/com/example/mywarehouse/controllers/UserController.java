package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.enums.Role;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

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

    @GetMapping("/user/profile/{id}")
    public String profile(@PathVariable("id")Integer id, Principal principal, Model model){
        User user = userRepository.findByUserId(id);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "profile";
    }
    @GetMapping("/user/profile/{id}/smth")
    public String profileF(@PathVariable("id")Integer id, Model model){
        User user = userRepository.findByUserId(id);
        model.addAttribute("user", user);
        //model.addAttribute("avatar",user.getAvatar());
        return "profile-upd";
    }



    @PostMapping("/user/profile/{id}")
    public String prof(@PathVariable("id")Integer id, @RequestParam String username, @RequestParam String name,
                       @RequestParam MultipartFile file) throws IOException {
        User user = userRepository.findByUserId(id);
        userService.updateU(user,username,name,file);
        return "redirect:/";
    }
    @PostMapping("/")
    public String hi(Principal principal, Model model){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        return "hello";
    }

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
