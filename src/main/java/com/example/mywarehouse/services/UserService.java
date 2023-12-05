package com.example.mywarehouse.services;

import com.example.mywarehouse.models.User;

import java.util.List;

public interface UserService {
    boolean createUser(User user);
    List<User> allUsers();
}
