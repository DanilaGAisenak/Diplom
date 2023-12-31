package com.example.mywarehouse.services;

import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ProductService {
   List<Product> listProducts(String name, Principal principal);
    void saveProduct(Principal principal, Product product,
                     MultipartFile image1, MultipartFile image2,
                     MultipartFile image3, String wh) throws IOException;
    void deleteProduct(Integer id);
    void updateProduct(Integer id, String name, String category, Float price, Integer img_link,
                       Integer tax, Float production_price, Warehouse warehouse, /*Integer user,*/
                       MultipartFile file1, MultipartFile file2, MultipartFile file3, User user) throws IOException;
    Product getProductById(Integer id);

}
