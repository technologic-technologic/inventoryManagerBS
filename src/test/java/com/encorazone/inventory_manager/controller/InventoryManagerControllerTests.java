package com.encorazone.inventory_manager.controller;

import com.encorazone.inventory_manager.domain.*;
import com.encorazone.inventory_manager.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class InventoryManagerControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    @DisplayName("returns selected page with products when getting all products")
    void getAll_returnsPage() throws Exception {
        ProductListResponse payload = new ProductListResponse(List.of(sampleProductRes()), 1);
        given(inventoryService.getAll(0, 10)).willReturn(payload);

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test
    @DisplayName("returns selected page according to de filter/sort orders")
    void findByFilter_passesPageable() throws Exception {
        ProductListResponse payload = new ProductListResponse(List.of(sampleProductRes()), 1);
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        Pageable expected = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("price")));
        given(inventoryService.findByNameAndCategoryAndStockQuantity(
                "Watermelon", "food", 0, expected))
                .willReturn(payload);

        mvc.perform(get("/products/filters")
                        .param("name", "Watermelon")
                        .param("category", "food")
                        .param("stockQuantity", "0")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "price,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));
        verify(inventoryService)
                .findByNameAndCategoryAndStockQuantity(
                        eq("Watermelon"),
                        eq("food"),
                        eq(0),
                        captor.capture());

        Pageable p = captor.getValue();
        assert p.getPageNumber() == 1;
        assert p.getPageSize() == 10;
        Sort.Order o = p.getSort().getOrderFor("price");
        assert o != null && o.getDirection() == Sort.Direction.DESC;
    }

    @Test
    @DisplayName("returns small product descriptions corresponding to the created product")
    void create_returnsShortResp() throws Exception {
        ProductShortResponse created = sampleProductShort();
        given(inventoryService.create(nullable(Product.class))).willReturn(created);

        Product toCreate = sampleProduct();
        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Watermelon")));
    }

    @Test
    @DisplayName("returns small product descriptions corresponding to the updated product")
    void update_returnsShortResp() throws Exception {
        ProductShortResponse updated = sampleProductShort();
        UUID id = updated.getId();
        given(inventoryService.update(eq(id), nullable(Product.class))).willReturn(Optional.of(updated));

        mvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Watermelon")));
    }

    @Test
    @DisplayName("returns 404-Not Found when no product id corresponds to the sent one - update")
    void update_returnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(inventoryService.update(eq(id), nullable(Product.class))).willReturn(Optional.empty());

        mvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("returns small product descriptions corresponding to the marked out of stock product")
    void markOutOfStock_returnsShortResp() throws Exception {
        ProductShortResponse resp = sampleProductShort();
        UUID id = resp.getId();
        given(inventoryService.markOutOfStock(id)).willReturn(Optional.of(resp));

        mvc.perform(patch("/products/{id}/outofstock", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Watermelon")));
    }

    @Test
    @DisplayName("returns 404-Not Found when no product id corresponds to the sent one - markOutOfStock")
    void markOutOfStock_returnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(inventoryService.markOutOfStock(id)).willReturn(Optional.empty());

        mvc.perform(patch("/products/{id}/outofstock", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("returns small product descriptions corresponding to the restored stock product")
    void restoreStock_returnsShortResp() throws Exception {
        ProductShortResponse resp = sampleProductShort();
        UUID id = resp.getId();
        given(inventoryService.updateStock(id, 10)).willReturn(Optional.of(resp));

        mvc.perform(patch("/products/{id}/instock", id).param("stockQuantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Watermelon")));
    }

    @Test
    @DisplayName("returns 404-Not Found when no product id corresponds to the sent one - restoreStock")
    void restoreStock_returnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(inventoryService.updateStock(id, 10)).willReturn(Optional.empty());

        mvc.perform(patch("/products/{id}/instock", id).param("stockQuantity", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("returns 204-No Content when deleting a product")
    void delete_returnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(inventoryService).delete(id);

        mvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("returns a list with all the categories with products")
    void fetchCategories_returnsCategories() throws Exception {
        given(inventoryService.fetchCategories()).willReturn(Optional.of(List.of("food", "drinks")));

        mvc.perform(get("/products/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItems("food", "drinks")));
    }

    @Test
    @DisplayName("returns 404-Not Found when no category is available")
    void fetchCategories_returnsNotFound() throws Exception {
        given(inventoryService.fetchCategories()).willReturn(Optional.empty());

        mvc.perform(get("/products/categories"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("returns Inventory Summary when calling for metrics")
    void fetchSummary_returnsInventorySummary() throws Exception {
        InventorySummaryResponse row = new InventorySummaryResponse("food", 100L, BigDecimal.valueOf(2500), BigDecimal.valueOf(25));
        given(inventoryService.fetchInventorySummary()).willReturn(Optional.of(List.of(row)));

        mvc.perform(get("/products/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category", is("food")));
    }

    @Test
    @DisplayName("returns 404-Not Found when no product in stock")
    void fetchSummary_returnsNotFound() throws Exception {
        given(inventoryService.fetchInventorySummary()).willReturn(Optional.empty());

        mvc.perform(get("/products/summary"))
                .andExpect(status().isNotFound());
    }

    private Product sampleProduct() {
        Product p = new Product();
        p.setName("Melon");
        p.setCategory("food");
        p.setUnitPrice(BigDecimal.valueOf(25));
        p.setStockQuantity(5);
        return p;
    }

    private ProductResponse sampleProductRes() {
        return new ProductResponse(
                UUID.randomUUID(),
                "Watermelon",
                "food",
                BigDecimal.valueOf(20),
                null,
                10,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    private ProductShortResponse sampleProductShort() {
        return new ProductShortResponse(
                UUID.randomUUID(),
                "Watermelon",
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
