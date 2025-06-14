package com.encorazone.inventory_manager.repository;

import com.encorazone.inventory_manager.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
