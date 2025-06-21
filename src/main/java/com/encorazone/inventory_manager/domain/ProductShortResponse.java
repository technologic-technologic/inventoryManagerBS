package com.encorazone.inventory_manager.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductShortResponse {
    private UUID id;

    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public ProductShortResponse(
            UUID id,
            String name,
            LocalDateTime creationDate,
            LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }
}
