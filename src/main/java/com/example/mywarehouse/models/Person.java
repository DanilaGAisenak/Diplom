package com.example.mywarehouse.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personId")
    private Integer personId;
    private String name;
    private String surname;
    private Integer age;
    @OneToOne(mappedBy = "person")
    private User user;
}
