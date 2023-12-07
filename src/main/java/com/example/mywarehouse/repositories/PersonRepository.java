package com.example.mywarehouse.repositories;

import com.example.mywarehouse.models.Person;
import com.example.mywarehouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    //Person findByPersonName(String name);
    Person findByPersonId(Integer id);
    List<Person> findPersonByUser(User user);
}
