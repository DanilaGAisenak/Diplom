package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Image;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.enums.Role;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public void updateUser(User user, String username, String name, String[] role, MultipartFile img1) throws IOException {
        Image img;
        if (img1.getSize()!=0){
            img = toImageEntity(img1);
            img.setPreviewImage(true);
            //user.setAvatar(img);
        }
        user.setUsername(username);
        user.setName(name);
        user.getRoles().clear();
        for (String rl : role){
            user.getRoles().add(Role.valueOf(rl));
        }

        userRepository.save(user);
    }

    public void updateU(User user, String username, String name, MultipartFile img1) throws IOException {
        Image img;
        if (img1.getSize()!=0){
            img = toImageEntity(img1);
            img.setPreviewImage(true);
            //user.setAvatar(img);
        }
        user.setUsername(username);
        user.setName(name);
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

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        String name = file.getName();
        StringBuilder res = new StringBuilder();
        for (int i=0;i<name.length();i++) res.append(name.charAt(i));
        image.setName(res.toString());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
