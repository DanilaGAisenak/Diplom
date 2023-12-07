package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.repositories.CompanyRepository;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    @Override
    public List<Company> listCompanies(String name, Principal principal) {
        if (name != null) return companyRepository.findByName(name);
        User user = getUserByPrincipal(principal);
        return companyRepository.findAllByUser(user);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    @Override
    public void saveCompany(Company company, Principal principal) {
        company.setUser(getUserByPrincipal(principal));
        companyRepository.save(company);
    }

    @Override
    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }

    @Override
    public void updateCompany(Integer id, String name, String address) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.get()!=null){
            company.get().setName(name);
            company.get().setAddress(address);
        }
        companyRepository.save(company.get());
    }

    @Override
    public Company getCompanyById(Integer id) {
        return companyRepository.findById(id).orElse(null);
    }
}
