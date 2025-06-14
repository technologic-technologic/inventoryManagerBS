package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll(String filter, String sort, int page, int size) {
        // Simplified: no filter/sort logic, just pagination
        return productRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> update(Long id, Product newProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(newProduct.getName());
            existing.setCategory(newProduct.getCategory());
            existing.setUnitPrice(newProduct.getUnitPrice());
            existing.setExpirationDate(newProduct.getExpirationDate());
            existing.setQuantityInStock(newProduct.getQuantityInStock());
            return productRepository.save(existing);
        });
    }

    @Override
    public Optional<Product> markOutOfStock(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setQuantityInStock(0);
            return productRepository.save(product);
        });
    }

    @Override
    public Optional<Product> restoreStock(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setQuantityInStock(10); // Default restore value
            return productRepository.save(product);
        });
    }
}
