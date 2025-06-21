package com.encorazone.inventory_manager.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;

    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public ProductResponse(UUID id, String name, LocalDateTime creationDate, LocalDateTime updateDate) {
    }
}
