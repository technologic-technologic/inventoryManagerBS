package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.domain.ProductResponse;
import com.encorazone.inventory_manager.repository.ProductRepository;
import com.encorazone.inventory_manager.mapper.ProductMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public ProductResponse create(Product product) {
        return ProductMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public Optional<ProductResponse> update(UUID id, Product newProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(newProduct.getName());
            existing.setCategory(newProduct.getCategory());
            existing.setUnitPrice(newProduct.getUnitPrice());
            existing.setExpirationDate(newProduct.getExpirationDate());
            existing.setStockQuantity(newProduct.getStockQuantity());
            return productRepository.save(existing);
        }).map(ProductMapper::toProductResponse);
    }

    @Override
    public void delete(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Id " + id + "not found in db");
        }
    }

    @Override
    public Optional<ProductResponse> markOutOfStock(UUID id) {
        return productRepository.findById(id).map(product -> {
            if (product.getStockQuantity() > 0) {
                product.setStockQuantity(0);
                return productRepository.save(product);
            } else {
                return product;
            }
        }).map(ProductMapper::toProductResponse);
    }

    @Override
    public Optional<ProductResponse> updateStock(UUID id, Integer stock) {
        return productRepository.findById(id).map(product -> {
            product.setStockQuantity(stock);
            return productRepository.save(product);
        }).map(ProductMapper::toProductResponse);
    }

    @Override
    public List<Product> findByNameAndCategoryAndStockQuantity(String name, String category,
                                                               Integer stockQuantity, Pageable pageable) {
        Specification<Product> spec = ProductFilter.nameContains(name)
                .and(ProductFilter.categoryContains(category))
                .and(ProductFilter.quantityEquals(stockQuantity));

        Page<Product> page = productRepository.findAll(spec, pageable);
        return page.getContent();
    }

}
