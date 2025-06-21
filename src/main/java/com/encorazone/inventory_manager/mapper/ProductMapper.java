package com.encorazone.inventory_manager.mapper;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.domain.ProductResponse;

public class ProductMapper {
    public static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCreationDate(),
                product.getUpdateDate()
        );
    }
}
