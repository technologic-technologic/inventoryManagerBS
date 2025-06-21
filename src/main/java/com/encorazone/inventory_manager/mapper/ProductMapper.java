package com.encorazone.inventory_manager.mapper;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.domain.ProductListResponse;
import com.encorazone.inventory_manager.domain.ProductResponse;
import com.encorazone.inventory_manager.domain.ProductShortResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductShortResponse toProductShortResponse(Product product) {
        return new ProductShortResponse(
                product.getId(),
                product.getName(),
                product.getCreationDate(),
                product.getUpdateDate()
        );
    }

    public static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getUnitPrice(),
                product.getExpirationDate(),
                product.getStockQuantity(),
                product.getCreationDate(),
                product.getUpdateDate()
        );
    }

    public static ProductListResponse toProductListResponse(List<Product> products, Integer totalPages) {
        return new ProductListResponse(
                products.stream()
                        .map(ProductMapper::toProductResponse)
                        .collect(Collectors.toList()),
                totalPages);
    }
}
