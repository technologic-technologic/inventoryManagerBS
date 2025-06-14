package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    List<Product> getAll(String filter, String sort, int page, int size);

    Product create(Product product);

    Optional<Product> update(UUID id, Product product);

    Optional<Product> markOutOfStock(UUID id);

    Optional<Product> restoreStock(UUID id);
}
