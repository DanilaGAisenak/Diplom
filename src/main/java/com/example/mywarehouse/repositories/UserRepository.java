package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByUserId(Integer id);
    User findByMasterId(Integer id);
    List<User> findAllByRoles(Role role);
    //@Qery(value ="SELECT * from user  where user.masterId IS NOT NULL", nativeQuery = true)
    List<User> findAllByMasterIdIsNotNull();
    List<User> findAllByMasterId(Integer id);
    List<User> findAllByUserId(Integer id);
}
