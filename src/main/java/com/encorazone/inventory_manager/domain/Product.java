package com.encorazone.inventory_manager.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "ID", columnDefinition = "RAW(16)")
    private UUID id;

    @Column(nullable = false, length = 120, name = "NAME")
    private String name;

    @Column(nullable = false, name = "CATEGORY")
    private String category;

    @Column(nullable = false, name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;

    @Column(nullable = false, name = "STOCK_QUANTITY")
    private Integer stockQuantity;

    @Column(updatable = false, name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @PrePersist
    public void onCreate() {
        creationDate = LocalDateTime.now();
        updateDate = creationDate;
    }

    @PreUpdate
    public void onUpdate() {
        updateDate = LocalDateTime.now();
    }


}
