package com.example.webshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.webshop.model.Product;
import com.example.webshop.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, String sort) {
        List<Product> products = productRepository.findAll();

        // Filter by name if provided
        if (name != null && !name.isEmpty()) {
            products = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }

        // Filter by min price if provided
        if (minPrice != null) {
            products = products.stream()
                .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                .collect(Collectors.toList());
        }

        // Filter by max price if provided
        if (maxPrice != null) {
            products = products.stream()
                .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
        }

        // Sort the products
        if (sort.equalsIgnoreCase("ASC")) {
            products = products.stream()
                .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                .collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("DESC")) {
            products = products.stream()
                .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                .collect(Collectors.toList());
        }

        return products;
    }
}