package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.services.impl.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyServiceImpl companyService;

    @GetMapping("/companies")
    public String com(@RequestParam(name = "name", required = false) String name, Principal principal, Model model){
        model.addAttribute("companies",companyService.listCompanies(name, principal));
        model.addAttribute("user", companyService.getUserByPrincipal(principal));
        return "companies";
    }

    @GetMapping("/company/add")
    public String addCom(){ return "addCompany"; }

    @GetMapping("/company/{id}/update")
    public String upd(@PathVariable("id")Integer id, Model model){
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company",company);
        return "updateCompany";
    }



    @PostMapping("/company/{id}/update")
    public String update(@PathVariable("id")Integer id, @RequestParam String name, @RequestParam String address){
        companyService.updateCompany(id,name,address);
        return "redirect:/companies";
    }

    @PostMapping("/company/add")
    public String addC(Principal principal, Company company){
        companyService.saveCompany(company, principal);
        return "redirect:/companies";
    }

    @PostMapping("/company/delete/{id}")
    public String delCom(@PathVariable("id")Integer id){
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }
}
