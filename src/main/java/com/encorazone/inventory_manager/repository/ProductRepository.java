package com.encorazone.inventory_manager.repository;

import com.encorazone.inventory_manager.domain.InventorySummaryInterface;
import com.encorazone.inventory_manager.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Query("SELECT DISTINCT p.category FROM Product p")
    Optional<List<String>> findDistinctCategories();

    @Query("SELECT p.category AS category, COUNT(p) AS productsInStock, " +
            "SUM(p.unitPrice) AS valueInStock, AVG(p.unitPrice) AS averageValue " +
            "FROM Product p GROUP BY p.category")
    List<InventorySummaryInterface> findCategoriesSummary();
}
