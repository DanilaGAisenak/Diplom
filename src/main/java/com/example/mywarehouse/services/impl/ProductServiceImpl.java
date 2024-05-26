package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Image;
import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.models.User;
import com.example.mywarehouse.models.Warehouse;
import com.example.mywarehouse.repositories.UserRepository;
import com.example.mywarehouse.repositories.WarehouseRepository;
import com.example.mywarehouse.repositories.productRepository;
import com.example.mywarehouse.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final productRepository productRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final WarehouseRepository warehouseRepository;
    @Override
    public List<Product> listProducts(String name, Principal principal){
        if (name != null) return productRepository.findByName(name);
        User user = getUserByPrincipal(principal);
        return productRepository.findAllByUser(user);
    }

    public List<Product> listThem(String name, Principal principal){
        if (name != null) return productRepository.findByName(name);
        User user = userRepository.findByUserId(userService.getUserByPrincipal(principal).getMasterId());
        List<Product> princip = productRepository.findAllByUser(userService.getUserByPrincipal(principal));
        List<Product> my = productRepository.findAllByUser(user);
        for (Product prod: my ) {
            princip.add(prod);
        }
        return princip;
    }

    @Override
    public void saveProduct(Principal principal, Product product,
                            MultipartFile image1, MultipartFile image2,
                            MultipartFile image3, String whName) throws IOException {
        User user = new User();
        if( userService.getUserByPrincipal(principal).getMasterId() != null ){
            user = userRepository.findByUserId(userService.getUserByPrincipal(principal).getMasterId());
        }
        else user  =userService.getUserByPrincipal(principal);
        product.setUser(user);
        List<Warehouse> whs = warehouseRepository.findAllByUser(user);
        Warehouse wh = new Warehouse();
        for (Warehouse whouse: whs ) {
            if (whouse.getName().equals(whName)) wh = whouse;
        }
        //Warehouse wh = warehouseRepository.findWarehouseByName(whName);
        product.setWarehouse(wh);
        Image img1;
        Image img2;
        Image img3;
        if (image1.getSize() != 0){
            img1 = toImageEntity(image1);
            img1.setPreviewImage(true);
            product.addImage(img1);
        }
        if (image2.getSize() != 0){
            img2 = toImageEntity(image2);
            img2.setPreviewImage(false);
            product.addImage(img2);
        }
        if (image3.getSize() != 0){
            img3 = toImageEntity(image3);
            img3.setPreviewImage(false);
            product.addImage(img3);
        }
        Product productFromDb = productRepository.save(product);
        if (!productFromDb.getImages().isEmpty())productFromDb.setImg_link(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        String name = file.getName();
        StringBuilder res = new StringBuilder();
        for (int i=0;i<name.length();i++) res.append(name.charAt(i));
        image.setName(res.toString());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Override
    public void deleteProduct(Integer id){
        productRepository.deleteById(id);
    }

    public void delImg(Integer id){
        Optional<Product> product = productRepository.findById(id);
        for (Image image:product.get().getImages()) {
            image.setProduct(null);
        }
        productRepository.save(product.get());
    }
    @Override
    public void updateProduct(Integer id, String name, String category, Float price, Integer img_link,
                              Integer tax, Float production_price, Warehouse warehouse, /*Integer user,*/
                              MultipartFile file1, MultipartFile file2, MultipartFile file3,
                              User user) throws IOException {
        Optional<Product> product = productRepository.findById(id);
        if (!(product.get()==null)){
            product.get().setName(name);
            product.get().setCategory(category);
            product.get().setPrice(price);
            if (img_link!=null)product.get().setImg_link(img_link);
            else product.get().setImg_link(0);
            product.get().setTax(tax);
            product.get().setProduction_price(production_price);
            product.get().setWarehouse(warehouse);
            product.get().setUser(user);
            for (Image image:product.get().getImages()) {
                image.setProduct(null);
            }
            productRepository.save(product.get());
            Image img1;
            Image img2;
            Image img3;
            if (file1.getSize() != 0){
                img1 = toImageEntity(file1);
                img1.setPreviewImage(true);
                product.get().addImage(img1);
            }
            if (file2.getSize() != 0){
                img2 = toImageEntity(file2);
                img2.setPreviewImage(false);
                product.get().addImage(img2);
            }
            if (file3.getSize() != 0){
                img3 = toImageEntity(file3);
                img3.setPreviewImage(false);
                product.get().addImage(img3);
            }
        }
        productRepository.save(product.get());
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
}
