package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Person;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.repositories.PersonRepository;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PersonRepository personRepository;

    @Override
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    @Override
    public Person getPersonById(Integer id) {
        return personRepository.findByPersonId(id);
    }

    @Override
    public void savePerson(Person person, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        user.setPerson(person);
        userRepository.save(user);
        person.setUser(user);
        personRepository.save(person);
    }

    @Override
    public void deletePerson(Integer id, User user) {
        user.setPerson(null);
        userRepository.save(user);
        personRepository.deleteById(id);
    }

    @Override
    public void updatePerson(Integer id, String name, String surname, Integer age) {
        Person person = personRepository.findByPersonId(id);
        if (person!=null) {
            person.setName(name);
            person.setSurname(surname);
            person.setAge(age);
        }
        personRepository.save(person);
    }
}
