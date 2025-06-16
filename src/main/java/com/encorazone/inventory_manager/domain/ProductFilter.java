package com.encorazone.inventory_manager.domain;

import org.springframework.data.jpa.domain.Specification;

public class ProductFilter {

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
     * Creates a specification for filtering products that have exactly the specified stock quantity.
     *
     * @param stock the stock quantity to match; if null, no filter is applied
     * @return a spec for matching stock quantities, or a null if the input is {@code null}
     */
    public static Specification<Product> quantityEquals(Integer stock) {
        return (root, query, cb) ->
                stock == null ? null :
                        cb.equal(root.get("stockQuantity"), stock);
    }
}
