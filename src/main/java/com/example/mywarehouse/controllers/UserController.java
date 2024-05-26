package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.enums.Role;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("er",null);
        return "log/login";
    }


    @GetMapping("/registration")
    public String registration(){
        return "log/registration";
    }


    @GetMapping("/")
    public String securityUrl(Principal principal, Model model,
                              @RequestParam(name = "successMessage", required = false)boolean successMessage){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        model.addAttribute("successMessage",successMessage);
        return "hello";
    }

    @GetMapping("/user/profile/{id}")
    public String profile(@PathVariable("id")Integer id, Principal principal, Model model){
        User user = userRepository.findByUserId(id);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "profile";
    }
    @GetMapping("/user/profile/{id}/smth")
    public String profileF(@PathVariable("id")Integer id, Model model, Principal principal){
        User user = userRepository.findByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("myUser",userService.getUserByPrincipal(principal).getUserId());
        //model.addAttribute("avatar",user.getAvatar());
        return "update/profile-upd";
    }
    @GetMapping("/user/profile/{id}/changePass")
    public String changePas(@PathVariable("id")Integer id, Model model){
        User user = userRepository.findByUserId(id);
        model.addAttribute("user",user);
        String message = "Введите текущий пароль:";
        model.addAttribute("mes",message);
        Boolean flag = false;
        model.addAttribute("flag", flag);
        return "update/changePassword";
    }
    @GetMapping("/moderators")
    public String moderators(Principal principal, Model model){
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user",user);
        model.addAttribute("users", userRepository.findAllByMasterId(user.getUserId()));
        return "modarators";
    }

    @GetMapping("/moderators/add")
    public String moderAdd(){ return "add/addMod"; }

    @GetMapping("/mod/update/{user}")
    public String userInfo(@PathVariable("user") User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "update/mod-update";
    }

    @PostMapping("/mod/update/{id}")
    public String saveUser(@PathVariable("id") Integer id, @RequestParam String username, @RequestParam String name) throws IOException {
        User user = userRepository.findByUserId(id);
        userService.updateU(user,username, name);
        return "redirect:/moderators";
    }



    @PostMapping("/user/profile/{id}/changePass")
    public String changePass(@PathVariable("id")Integer id, Model model, @RequestParam String pas){
        User user = userRepository.findByUserId(id);
        model.addAttribute("user", user);
        String message = "";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        if(passwordEncoder.matches(pas, user.getPassword())) {
            message = "Введите новый пароль";
            Boolean flag = true;
            model.addAttribute("mes", message);
            model.addAttribute("flag", flag);

        }
        else {
            Boolean flag = false;
            message = "Неверный пароль";
            model.addAttribute("mes", message);
            model.addAttribute("flag", flag);
        }
        return "update/changePassword";
    }
    @PostMapping("/user/profile/{id}/change")
    public String change(@PathVariable("id")Integer id, String pas, Principal principal){
        User user = userService.getUserByPrincipal(principal);
        user.setPassword(passwordEncoder.encode(pas));
        userRepository.save(user);
        return "redirect:/user/profile/{id}";
    }
    @PostMapping("/user/profile/{id}")
    public String prof(@PathVariable("id")Integer id, @RequestParam String username, @RequestParam String name
                       /*@RequestParam MultipartFile file*/) throws IOException {
        User user = userRepository.findByUserId(id);
        userService.updateU(user,username,name);
        return "redirect:/user/profile/{id}";
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
            return "log/registration";
        }
        //userService.createUser(user);
        return "redirect:/login";
    }
    @PostMapping("/reg")
    public String CreateModer(@RequestParam String name, @RequestParam String username, @RequestParam String password,  Model model, Principal principal){
        Integer masterUser = userService.getUserByPrincipal(principal).getUserId();
        userService.createMod(name, username, password, masterUser);

        return "redirect:/moderators";
    }

    @PostMapping("/mod/ban/{id}")
    public String modBan(@PathVariable("id") Integer id){
        userService.banUser(id);
        return "redirect:/moderators";
    }

    @PostMapping("/mod/delete/{id}")
    public String deleteU(@PathVariable("id") Integer id){
        userRepository.deleteById(id);
        return "redirect:/moderators";
    }
}
