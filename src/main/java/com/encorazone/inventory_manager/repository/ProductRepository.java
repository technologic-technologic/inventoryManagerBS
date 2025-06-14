package com.encorazone.inventory_manager.repository;

import com.encorazone.inventory_manager.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
