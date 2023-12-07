package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.repositories.WarehouseRepository;
import com.example.mywarehouse.services.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    @Override
    public List<Warehouse> listWarehouses(String name, Principal principal) {
        if (name != null) return warehouseRepository.findByName(name);
        User user = getUserByPrincipal(principal);
        return warehouseRepository.findAllByUser(user);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
       if (principal == null )return new User();
       return userRepository.findByUsername(principal.getName());
    }

    @Override
    public void saveWarehouse(Warehouse warehouse, Company company, Principal principal) {
        warehouse.setCompany(company);
    }

    @Override
    public void deleteWarehouse(Integer id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public void updateWarehouse(Integer id, String name, String address, String type) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);
        if (warehouse.get()!=null){
            warehouse.get().setName(name);
            warehouse.get().setAddress(address);
            warehouse.get().setType(type);
        }
        warehouseRepository.save(warehouse.get());
    }

    @Override
    public Warehouse getWarehouseById(Integer id) {
        return warehouseRepository.findByWarehouseId(id);
    }
}
