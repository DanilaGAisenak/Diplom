package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;
import com.example.mywarehouse.repositories.CompanyRepository;
import com.example.mywarehouse.services.impl.CompanyServiceImpl;
import com.example.mywarehouse.services.impl.WarehouseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseServiceImpl warehouseService;
    private final CompanyRepository companyRepository;
    private final CompanyServiceImpl companyService;

    @GetMapping("/warehouses")
    public String warehouses(@RequestParam(value = "name", required = false)String name, Principal principal, Model model){
        model.addAttribute("warehouses", warehouseService.listWarehouses(name, principal));
        model.addAttribute("user", warehouseService.getUserByPrincipal(principal));
        return "warehouses";
    }

    @GetMapping("/warehouses/add")
    public String addW(Principal principal, Model model){
        User user = warehouseService.getUserByPrincipal(principal);
        List<Company> list = companyService.list(user);
        model.addAttribute("companies", list);
        return "addWarehouse";
    }

    @GetMapping("/warehouses/{id}/update")
    public String updW(@PathVariable("id")Integer id, Model model, Principal principal){
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        model.addAttribute("wh",warehouse);
        User user = warehouseService.getUserByPrincipal(principal);
        List<Company> list = companyService.list(user);
        model.addAttribute("companies", list);
        return "updateWarehouse";
    }

    @GetMapping("/warehouses/delete/{id}")
    public String deleteW(@PathVariable("id")Integer id){
        warehouseService.deleteWarehouse(id);
        return "redirect:/warehouses";
    }


    @PostMapping("/warehouses/add")
    public String addWare(Principal principal, Warehouse warehouse, @RequestParam String comName){
        Company company = companyRepository.findCompanyByName(comName);
        warehouseService.saveWarehouse(warehouse, company, principal);
        return "redirect:/warehouses";
    }

    @PostMapping("/warehouses/{id}/update")
    public String updateW(@PathVariable("id")Integer id, @RequestParam String comName, @RequestParam String name,
                          @RequestParam String address, @RequestParam String type){
        warehouseService.updateWarehouse(id,name,address, type, comName);
        return "redirect:/warehouses";
    }
}
