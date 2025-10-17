package com.encorazone.inventory_manager.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductListResponse {
    private List<ProductResponse> products;

    private Integer totalPages;

    public ProductListResponse(List<ProductResponse> products, Integer totalPages) {
        this.products = products;
        this.totalPages = totalPages;
    }
}
