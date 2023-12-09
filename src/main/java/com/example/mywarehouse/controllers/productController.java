package com.example.mywarehouse.controllers;

import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.repositories.WarehouseRepository;
import com.example.mywarehouse.services.impl.ProductServiceImpl;
import com.example.mywarehouse.services.impl.UserServiceImpl;
import com.example.mywarehouse.services.impl.WarehouseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class productController {
    private final ProductServiceImpl productServiceImpl;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseServiceImpl warehouseService;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @GetMapping("/products")
    public String products(@RequestParam(name = "name", required = false) String name, Principal principal, Model model) {
        model.addAttribute("products", productServiceImpl.listProducts(name, principal));
        model.addAttribute("user",productServiceImpl.getUserByPrincipal(principal));
        return "products";
    }

    @GetMapping("/product/add")
    public String addProduct(Principal principal, Model model) {
        User user = productServiceImpl.getUserByPrincipal(principal);
        List<Warehouse> warehouses = warehouseRepository.findAllByUser(user);
        model.addAttribute("warehouse", warehouses);
        return "add/addProduct";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Integer id, Model model) {
        Product product = productServiceImpl.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }

    @GetMapping("/product/{id}/update")
    public String updateProduct(@PathVariable Integer id, Model model, Principal principal) {
        Product product = productServiceImpl.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        User user = productServiceImpl.getUserByPrincipal(principal);
        List<Warehouse> warehouses = warehouseRepository.findAllByUser(user);
        model.addAttribute("warehouse", warehouses);
        return "update/updateProduct";
    }

    @GetMapping("{id}/wrongSize")
    public String warning(@PathVariable Integer id, Model model){
        List<Integer> list = new ArrayList<>();
        list.add(id);
        model.addAttribute("id",list);
        return "warnings/warningSize";
    }

    @PostMapping("/product/{id}/updateS")
    public String updated(@PathVariable Integer id, @RequestParam String name, @RequestParam String category,
                          @RequestParam Float price, @RequestParam Integer img_link, @RequestParam Integer tax,
                          @RequestParam Float production_price, @RequestParam String warehouse, /*@RequestParam Integer user,*/
                          @RequestParam MultipartFile file1, @RequestParam MultipartFile file2, @RequestParam MultipartFile file3,
                          Principal principal) throws IOException {
        if (file1.getSize() <= (1024*1024)){
            if (file2.getSize() <= (1024*1024)){
                if (file3.getSize() <= (1024*1024)){
                    Warehouse wh = warehouseRepository.findWarehouseByName(warehouse);
                    User user = warehouseService.getUserByPrincipal(principal);
                    productServiceImpl.updateProduct(id, name, category, price, img_link, tax, production_price,
                            wh, /*user,*/ file1, file2, file3, user);
                    return "redirect:/products";
                }
            }
        }
        return "redirect:/{id}/wrongSize";
    }

    @PostMapping("/product/create")
    public String createProduct(Principal principal, Product product, @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, @RequestParam("wah") String warehouse) throws IOException {
        productServiceImpl.saveProduct(principal, product, file1, file2, file3, warehouse);
        return "redirect:/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productServiceImpl.deleteProduct(id);
        return "redirect:/products";
    }
}
