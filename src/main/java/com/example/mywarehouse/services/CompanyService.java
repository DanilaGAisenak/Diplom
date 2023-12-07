package com.example.mywarehouse.services;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.User;

import java.security.Principal;
import java.util.List;

public interface CompanyService {
    List<Company> listCompanies(String name, Principal principal);
    User getUserByPrincipal(Principal principal);
    void saveCompany(Company company, Principal principal);
    void deleteCompany(Integer id);
    void updateCompany(Integer id, String name, String address);
    Company getCompanyById(Integer id);
}
