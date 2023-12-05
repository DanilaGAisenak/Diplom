package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.enums.Role;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user, String username, String name, String[] role) {
        user.setUsername(username);
        user.setName(name);
        user.getRoles().clear();
        for (String rl : role){
            user.getRoles().add(Role.valueOf(rl));
        }
//        Set<String> roles = Arrays.stream(Role.values())
//                .map(Role::name)
//                .collect(Collectors.toSet());
//        user.getRoles().clear();
//        for (String rl : role) {
//            if (roles.contains(rl)){
//                user.getRoles().add(Role.valueOf(rl));
//            }
//        }
        userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    public void banUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (!user.isActive()) user.setActive(true);
            else user.setActive(false);
        }
        userRepository.save(user);
    }
}
