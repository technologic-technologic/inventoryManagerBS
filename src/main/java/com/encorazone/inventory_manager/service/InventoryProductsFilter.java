package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;
import org.springframework.data.jpa.domain.Specification;

public class InventoryProductsFilter {

    /**
     * Creates a specification for filtering products whose names contain the given substring,
     *
     * @param name the name substring to search for; if null or blank, no filter is applied
     * @return a Specification for matching product names, or  null if the input is invalid
     */
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank() ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Creates a specification for filtering products where the category contain the given substring,
     *
     * @param category the name substring to search for; if null or blank, no filter is applied
     * @return a Specification for matching product categories, or  null if the input is invalid
     */
    public static Specification<Product> categoryContains(String category) {
        return (root, query, cb) ->
                category == null || category.isBlank() ? null :
                        cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%");
    }

    /**
     * Creates a specification for filtering the availability of the products.
     *
     * @param stock the stock quantity parameter:
     *              0 -> don't filter, 1 -> only in stock, 2 -> Not in stock, 3 -> to reset status
     * @return a spec for matching stock quantities, or a null if the input is null
     */
    public static Specification<Product> quantityEquals(Integer stock) {
        return switch (stock) {
            case 0, 3 -> (root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("stockQuantity"), 0);
            case 1 -> (root, query, cb) ->
                    cb.greaterThan(root.get("stockQuantity"), 0);
            case 2 -> (root, query, cb) ->
                    cb.equal(root.get("stockQuantity"), 0);
            default -> null;
        };
    }
}
