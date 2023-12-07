package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.Person;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.repositories.PersonRepository;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.PersonService;
import com.example.mywarehouse.services.impl.PersonServiceImpl;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PersonController {
    private final PersonServiceImpl personService;
    private final PersonRepository personRepository;
    private final UserServiceImpl userService;

    @GetMapping("/person")
    public String person(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("person", personRepository.findPersonByUser(user));
        model.addAttribute("user", user);
        return "person";
    }

    @GetMapping("/person/add")
    public String addPerson(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "addPerson";
    }

    @GetMapping("/person/delete/{id}")
    public String deletePerson(@PathVariable("id") Integer id, Principal principal) {
        personService.deletePerson(id, personService.getUserByPrincipal(principal));
        return "redirect:/person";
    }

    @GetMapping("/person/update/{id}")
    public String updPerson(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("person", personRepository.findByPersonId(id));
        return "updatePerson";
    }


    @PostMapping("/person/update/{id}")
    public String updatePerson(@PathVariable("id") Integer id, @RequestParam String name,
                               @RequestParam String surname, @RequestParam Integer age) {
        personService.updatePerson(id, name, surname, age);
        return "redirect:/person";
    }

    @PostMapping("/person/add")
    public String addP(Principal principal, Person person) {
        personService.savePerson(person, principal);
        return "redirect:/person";
    }

}
