package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.domain.ProductListResponse;
import com.encorazone.inventory_manager.domain.ProductShortResponse;
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
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductListResponse getAll(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        return ProductMapper.toProductListResponse(products.getContent(),products.getTotalPages());
    }

    @Override
    public ProductShortResponse create(Product product) {
        return ProductMapper.toProductShortResponse(productRepository.save(product));
    }

    @Override
    public Optional<ProductShortResponse> update(UUID id, Product newProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(newProduct.getName());
            existing.setCategory(newProduct.getCategory());
            existing.setUnitPrice(newProduct.getUnitPrice());
            existing.setExpirationDate(newProduct.getExpirationDate());
            existing.setStockQuantity(newProduct.getStockQuantity());
            return productRepository.save(existing);
        }).map(ProductMapper::toProductShortResponse);
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
    public Optional<ProductShortResponse> markOutOfStock(UUID id) {
        return productRepository.findById(id).map(product -> {
            if (product.getStockQuantity() > 0) {
                product.setStockQuantity(0);
                return productRepository.save(product);
            } else {
                return product;
            }
        }).map(ProductMapper::toProductShortResponse);
    }

    @Override
    public Optional<ProductShortResponse> updateStock(UUID id, Integer stock) {
        return productRepository.findById(id).map(product -> {
            product.setStockQuantity(stock);
            return productRepository.save(product);
        }).map(ProductMapper::toProductShortResponse);
    }

    @Override
    public ProductListResponse findByNameAndCategoryAndStockQuantity(String name, String category,
                                                                     Integer stockQuantity, Pageable pageable) {
        Specification<Product> spec = InventoryProductsFilter.nameContains(name)
                .and(InventoryProductsFilter.categoryContains(category))
                .and(InventoryProductsFilter.quantityEquals(stockQuantity));

        Page<Product> page = productRepository.findAll(spec, pageable);
        return ProductMapper.toProductListResponse(page.getContent(), page.getTotalPages());
    }

    @Override
    public Optional<List<String>> fetchCategories(){
        return productRepository.findDistinctCategories();
    }

}
