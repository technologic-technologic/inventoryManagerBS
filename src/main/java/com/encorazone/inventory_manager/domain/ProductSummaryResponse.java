package com.encorazone.inventory_manager.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSummaryResponse {
    private Integer productsInStock;

    private BigDecimal valueInStock;

    private BigDecimal averageValue;

    public ProductSummaryResponse(Integer productsInStock, BigDecimal valueInStock, BigDecimal averageValue) {
        this.productsInStock = productsInStock;
        this.valueInStock = valueInStock;
        this.averageValue = averageValue;
    }
}
