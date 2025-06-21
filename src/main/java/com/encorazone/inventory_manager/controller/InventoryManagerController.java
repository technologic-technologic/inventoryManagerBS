package com.encorazone.inventory_manager.controller;

import com.encorazone.inventory_manager.domain.Product;
import com.encorazone.inventory_manager.domain.ProductListResponse;
import com.encorazone.inventory_manager.domain.ProductShortResponse;
import com.encorazone.inventory_manager.service.InventoryService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
final class InventoryManagerController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * Edpoin to get all the elemnts from database, no sorting nor filtering
     * Just pagination for the client usability
     *
     * @param page represents the page for the table. Example 0.
     * @param size represents the number of elements per page, default is 10. Example 20.
     * @return response status amd a list containing the pagexsize elements
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(inventoryService.getAll(page, size));
    }

    /**
     * Endpoint for filtered data retrieving, including name, category and availability
     * filtering from DB objects.
     *
     * @param name          represent the name of the product. Example, Watermelon.
     * @param category      represents the category the element is part of; like food.
     * @param stockQuantity represents the amount of elements in the inventory. Example 10.
     * @param pageable      Builtin object for sorting and pagination, the API asks for the json by itself
     * @return responsse status and a list containing the pagexsixe elements
     * complying with the sort and filter parameters
     */
    @GetMapping("/filters")
    public ResponseEntity<ProductListResponse> findByFilter(
            @ModelAttribute @RequestParam(required = false) String name,
            @ModelAttribute @RequestParam(required = false) String category,
            @ModelAttribute @RequestParam(required = false, defaultValue = "0") Integer stockQuantity,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(inventoryService.findByNameAndCategoryAndStockQuantity(
                name, category, stockQuantity, pageable));
    }

    /**
     * Endpoint to create a new product
     *
     * @param product object representing the product to be added to the inventory
     * @return status. Example 200(OK)
     */
    @PostMapping
    public ResponseEntity<ProductShortResponse> create(@RequestBody Product product) {
        return ResponseEntity.ok(inventoryService.create(product));
    }

    /**
     * endpoint to update a product
     *
     * @param id      represents the DB/BS internal id fro managing elements.
     *                Example 785c0229-b7e5-4ea4-853b-fa5ad4eb84f4
     * @param product Object with the changes fto be made to the product
     * @return status. Example 500 (Internal server error)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductShortResponse> update(@PathVariable UUID id, @RequestBody Product product) {
        return inventoryService.update(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * endpoint to automatically set stock to 0
     *
     * @param id Represents the id of the element we want the stock to be 0
     * @return status. Example 200
     */
    @PatchMapping("/{id}/outofstock")
    public ResponseEntity<ProductShortResponse> markOutOfStock(@PathVariable UUID id) {
        return inventoryService.markOutOfStock(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * endpoint to automatically set stock to a given number, By default 10
     *
     * @param id            Represents the id of the element we want the stock to be 0
     * @param stockQuantity Represents the amount to put into stock. Example 10
     * @return status. Example 200(OK)
     */
    @PatchMapping("/{id}/instock")
    public ResponseEntity<ProductShortResponse> restoreStock(@PathVariable UUID id,
                                                             @RequestParam(defaultValue = "10") Integer stockQuantity) {
        return inventoryService.updateStock(id, stockQuantity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to delete products
     *
     * @param id Represemts tje id of the element to be deleted
     * @return status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        inventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}