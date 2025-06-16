package com.encorazone.inventory_manager.repository;

import com.encorazone.inventory_manager.domain.Product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    /**
     * This method was my second try to filter and sort data. It filters well,
     * cannot sort, explained better on the DEMO if I don't forget, Probably will.
     * Basically the method was filtering always the availability,
     * setting the stockQuantity to 0, meaning if the product had a stock different
     * to 0, It wouldn't appear in the resulting list.
     *
     * @param name            name
     * @param category        category
     * @param quantityInStock availability of the product
     * @param pageable        objet for sorting and pagination
     * @return a list of product matchin the description
     */
    List<Product> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStockQuantity(
            String name, String category, Integer quantityInStock, Pageable pageable);
}
