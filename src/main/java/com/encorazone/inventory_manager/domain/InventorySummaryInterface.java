package com.encorazone.inventory_manager.domain;

import java.math.BigDecimal;

public interface InventorySummaryInterface {
    String getCategory();
    Long getProductsInStock();
    BigDecimal getValueInStock();
    BigDecimal getAverageValue();
}