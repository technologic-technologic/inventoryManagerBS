package com.encorazone.inventory_manager.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;

    private String name;

    private String category;

    private BigDecimal unitPrice;

    private LocalDate expirationDate;

    private Integer stockQuantity;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public ProductResponse(
            UUID id,
            String name,
            String category,
            BigDecimal unitPrice,
            LocalDate expirationDate,
            Integer stockQuantity,
            LocalDateTime creationDate,
            LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.expirationDate = expirationDate;
        this.stockQuantity = stockQuantity;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }
}
