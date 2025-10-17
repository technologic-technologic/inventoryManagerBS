package com.encorazone.inventory_manager.service;

import com.encorazone.inventory_manager.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class InventoryProductsFilterTests {

    @Test
    void nameContains_builds_like_lowercased() {
        @SuppressWarnings("unchecked")
        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        @SuppressWarnings("unchecked")
        Path<String> path = (Path<String>) mock(Path.class);
        @SuppressWarnings("unchecked") Expression<String> lower = (Expression<String>) mock(Expression.class);
        Predicate pred = mock(Predicate.class);
        given(root.<String>get("name")).willReturn((path));
        given(cb.lower(path)).willReturn(lower);
        given(cb.like(lower, "%apple%")).willReturn(pred);

        Specification<Product> spec = InventoryProductsFilter.nameContains("Apple");
        assertSame(pred, spec.toPredicate(root, query, cb));
    }

    @Test
    void categoryContains_returns_null_when_blank() {
        Specification<Product> spec = InventoryProductsFilter.categoryContains("   ");
        @SuppressWarnings("unchecked")
        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Test
    void quantityEquals_inStock_builds_gt_zero() {
        @SuppressWarnings("unchecked")
        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        @SuppressWarnings("unchecked")
        Path<Integer> path = (Path<Integer>) mock(Path.class);
        Predicate pred = mock(Predicate.class);

        given(root.<Integer>get("stockQuantity")).willReturn(path);
        given(cb.greaterThan(path, 0)).willReturn(pred);

        Specification<Product> spec = InventoryProductsFilter.quantityEquals(1);
        assertNotNull(spec);
        assertSame(pred, spec.toPredicate(root, query, cb));
    }

    @Test
    void quantityEquals_outOfStock_builds_eq_zero() {
        @SuppressWarnings("unchecked")
        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        @SuppressWarnings("unchecked")
        Path<Integer> path = (Path<Integer>) mock(Path.class);
        Predicate pred = mock(Predicate.class);

        given(root.<Integer>get("stockQuantity")).willReturn(path);
        given(cb.equal(path, 0)).willReturn(pred);

        Specification<Product> spec = InventoryProductsFilter.quantityEquals(2);
        assertNotNull(spec);
        assertSame(pred, spec.toPredicate(root, query, cb));
    }
}
