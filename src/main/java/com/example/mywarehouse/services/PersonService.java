package com.example.mywarehouse.services;

import com.example.mywarehouse.models.Person;
import com.example.mywarehouse.models.User;
import org.springframework.stereotype.Service;

import java.security.Principal;


public interface PersonService {
    User getUserByPrincipal(Principal principal);
    Person getPersonById(Integer id);
    void savePerson(Person person, Principal principal);
    void deletePerson(Integer id, User user);
    void updatePerson(Integer id, String name, String surname, Integer age);
}
