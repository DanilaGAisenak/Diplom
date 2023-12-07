package com.example.mywarehouse.services;

import com.example.mywarehouse.models.Company;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;

import java.security.Principal;
import java.util.List;

public interface WarehouseService {
    List<Warehouse> listWarehouses(String name, Principal principal);
    User getUserByPrincipal(Principal principal);
    void saveWarehouse(Warehouse warehouse, Company company, Principal principal);
    void deleteWarehouse(Integer id);
    void updateWarehouse(Integer id, String name, String address, String type, String comName);
    Warehouse getWarehouseById(Integer id);
}
