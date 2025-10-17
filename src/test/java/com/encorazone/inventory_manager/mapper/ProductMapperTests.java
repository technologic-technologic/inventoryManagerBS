package com.encorazone.inventory_manager.mapper;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.domain.InventorySummaryInterface;
import com.encorazone.inventory_manager.domain.ProductListResponse;
import com.encorazone.inventory_manager.domain.ProductResponse;
import com.encorazone.inventory_manager.domain.ProductShortResponse;
import com.encorazone.inventory_manager.domain.InventorySummaryResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTests {

    @Test
    void toProductShortResponse_mapsFields() {
        Product p = sampleProduct();
        ProductShortResponse r = ProductMapper.toProductShortResponse(p);
        assertEquals(p.getId(), r.getId());
        assertEquals(p.getName(), r.getName());
        assertEquals(p.getCreationDate(), r.getCreationDate());
        assertEquals(p.getUpdateDate(), r.getUpdateDate());
    }

    @Test
    void toProductResponse_mapsFields() {
        Product p = sampleProduct();
        ProductResponse r = ProductMapper.toProductResponse(p);
        assertEquals(p.getId(), r.getId());
        assertEquals(p.getName(), r.getName());
        assertEquals(p.getCategory(), r.getCategory());
        assertEquals(p.getUnitPrice(), r.getUnitPrice());
        assertEquals(p.getExpirationDate(), r.getExpirationDate());
        assertEquals(p.getStockQuantity(), r.getStockQuantity());
        assertEquals(p.getCreationDate(), r.getCreationDate());
        assertEquals(p.getUpdateDate(), r.getUpdateDate());
    }

    @Test
    void toProductListResponse_mapsListAndTotalPages() {
        Product p1 = sampleProduct();
        Product p2 = sampleProduct();
        p2.setId(UUID.randomUUID());
        p2.setName("Another");
        ProductListResponse r = ProductMapper.toProductListResponse(List.of(p1, p2), 7);
        assertEquals(2, r.getProducts().size());
        assertEquals(7, r.getTotalPages());
        assertEquals(p1.getId(), r.getProducts().get(0).getId());
        assertEquals(p2.getId(), r.getProducts().get(1).getId());
    }

    @Test
    void toInventorySummaryResponse_mapsFields() {
        InventorySummaryInterface row = sampleSummaryRow("food", 12L, new BigDecimal("345.67"), new BigDecimal("28.81"));
        InventorySummaryResponse r = ProductMapper.toInventorySummaryResponse(row);
        assertEquals("food", r.getCategory());
        assertEquals(12L, Long.valueOf(r.getProductsInStock()));
        assertEquals(new BigDecimal("345.67"), r.getValueInStock());
        assertEquals(new BigDecimal("28.81"), r.getAverageValue());
    }

    @Test
    void toInventorySummaryResponseList_mapsList() {
        List<InventorySummaryResponse> list = ProductMapper.toInventorySummaryResponseList(List.of(
                sampleSummaryRow("food", 10L, new BigDecimal("100.00"), new BigDecimal("10.00")),
                sampleSummaryRow("drinks", 5L, new BigDecimal("55.50"), new BigDecimal("11.10"))
        ));
        assertEquals(2, list.size());
        assertEquals("food", list.get(0).getCategory());
        assertEquals("drinks", list.get(1).getCategory());
    }

    private Product sampleProduct() {
        Product p = new Product();
        p.setId(UUID.randomUUID());
        p.setName("Watermelon");
        p.setCategory("food");
        p.setUnitPrice(new BigDecimal("19.99"));
        p.setExpirationDate(LocalDate.now().plusDays(30));
        p.setStockQuantity(7);
        p.setCreationDate(LocalDateTime.now().minusDays(1));
        p.setUpdateDate(LocalDateTime.now());
        return p;
    }

    private InventorySummaryInterface sampleSummaryRow(String category, Long inStock, BigDecimal value, BigDecimal avg) {
        return new InventorySummaryInterface() {
            @Override public String getCategory() { return category; }
            @Override public Long getProductsInStock() { return inStock; }
            @Override public BigDecimal getValueInStock() { return value; }
            @Override public BigDecimal getAverageValue() { return avg; }
        };
    }
}
