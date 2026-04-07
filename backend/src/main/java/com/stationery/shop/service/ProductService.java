package com.stationery.shop.service;

import com.stationery.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ProductService {
    Page<Product> getAllProducts(String search, Pageable pageable);
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    void deleteAll();
}
