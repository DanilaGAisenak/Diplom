package com.example.mywarehouse.services.impl;

import com.example.mywarehouse.models.Image;
import com.example.mywarehouse.models.Product;
import com.example.mywarehouse.repositories.productRepository;
import com.example.mywarehouse.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final productRepository productRepository;
    @Override
    public List<Product> listProducts(String name){
        if (name != null) return productRepository.findByName(name);
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(Product product, MultipartFile image1, MultipartFile image2, MultipartFile image3) throws IOException {
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
        productFromDb.setImg_link(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
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

    @Override
    public void updateProduct(Integer id, String name, String category, Float price, Integer img_link,
                              Integer tax, Float production_price, Integer warehouse, Integer user,
                              MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Optional<Product> product = productRepository.findById(id);
        if (!(product.get()==null)){
            product.get().setName(name);
            product.get().setCategory(category);
            product.get().setPrice(price);
            product.get().setImg_link(img_link);
            product.get().setTax(tax);
            product.get().setProduction_price(production_price);
            product.get().setWarehouse(warehouse);
            product.get().setUser(user);
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
