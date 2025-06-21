package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;

import com.encorazone.inventory_manager.domain.ProductListResponse;
import com.encorazone.inventory_manager.domain.ProductShortResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface InventoryService {

    /**
     * method to get all the elemnts from database, no sorting nor filtering
     * Just pagination for the client usability
     *
     * @param page represents the page for the table. Example 0.
     * @param size represents the number of elements per page, default is 10. Example 20.
     * @return a list containing the pagexsize elements
     */
    ProductListResponse getAll(int page, int size);

    /**
     * Method to create a new product and save it
     *
     * @param product object representing the product to be added to the inventory
     * @return the product creeated
     */
    ProductShortResponse create(Product product);

    /**
     * Updates an existing product identified by the given ID.
     *
     * @param id      the UUID of the product to update
     * @param product the updated product data
     * @return an Optional containing the updated product if found, or empty if not found
     */
    Optional<ProductShortResponse> update(UUID id, Product product);

    /**
     * Method to delet product
     *
     * @param id Reoresents the id of the element to be deleted
     */
    void delete(UUID id);

    /**
     * Method to automatically set stock to 0
     *
     * @param id Represents the id of the element we want the stock to be 0
     * @return an optional containing the updated product if the operation succeeded, or empty if not found
     */
    Optional<ProductShortResponse> markOutOfStock(UUID id);

    /**
     * method to automatically set stock to the given number
     *
     * @param id    Represents the id of the element we want the stock to be 0
     * @param stock Represents the amount to put into stock. Example 10
     * @return n optional containing the updated product if the operation succeeded, or empty if not found
     */
    Optional<ProductShortResponse> updateStock(UUID id, Integer stock);

    /**
     * Endpoint for filtered data retrieving, including name, category and availability
     * filtering from DB objects.
     *
     * @param name          represent the name of the product (or part of it). Example, Watermelon.
     * @param category      represents the category the element is part of; like food.
     * @param stockQuantity represents the amount of elements in the inventory. Example 10.
     * @param pageable      Builtin object for sorting and pagination, the API asks for the json by itself
     * @return a list of products matching the criteria
     */
    ProductListResponse findByNameAndCategoryAndStockQuantity(String name, String category,
                                                              Integer stockQuantity, Pageable pageable);
}
