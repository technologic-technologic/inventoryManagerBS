package com.encorazone.inventory_manager.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventorySummaryResponse{
    private String category;

    private Integer productsInStock;

    private BigDecimal valueInStock;

    private BigDecimal averageValue;

    public InventorySummaryResponse(String category, long productsInStock, BigDecimal valueInStock,
                                    BigDecimal averageValue) {
        this.category = category;
        this.productsInStock = (int) productsInStock;
        this.valueInStock = valueInStock;
        this.averageValue = averageValue;
    }
}
