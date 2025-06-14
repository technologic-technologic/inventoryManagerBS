package com.encorazone.inventory_manager.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Product {

    private Long id;

    private String name;

    private String category;

    private BigDecimal unitPrice;

    private LocalDate expirationDate;

    private Integer quantityInStock;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;


}
