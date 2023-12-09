package com.example.mywarehouse.models;

import com.example.mywarehouse.models.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "user")
//@Data
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;
    @Column(name="username", unique = true)
    private String username;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "image_id")
//    private Image avatar;
    @Column(name="password", length = 1000)
    private String password;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Product> products = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "user")
    private List<Company> companies = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Warehouse> warehouses = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personId", referencedColumnName = "personId")
    private Person person;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
    private LocalDateTime dateOfCreation;

    public boolean isUser(){
        if (getRoles().contains(Role.valueOf("ROLE_USER"))) return true;
        else return false;
    }

    @PrePersist
    private void init(){
        dateOfCreation = LocalDateTime.now();
    }

    //security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
