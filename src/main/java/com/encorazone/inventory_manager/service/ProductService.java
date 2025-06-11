package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAll(String filter, String sort, int page, int size);
    Product create(Product product);
    Optional<Product> update(Long id, Product product);
    Optional<Product> markOutOfStock(Long id);
    Optional<Product> restoreStock(Long id);
}
