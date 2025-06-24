# InventoryManagerBC
Breakable Toy 1 - Inventory Manager

# Inventory Management Application

This is a Spring Boot-based inventory management application designed to help manage product inventories effectively. It provides a RESTful API to create, update, and retrieve product information with filtering, sorting, and pagination capabilities. This application is built with future scalability in mind, including support for an easy transition from in-memory storage to a persistent database.

---

## Features

- **Product Management**
    - Create new products with name, category, quantity, unit price, and optional expiration date.
    - Edit product details including stock, price, and expiration date.
    - Mark products as in or out of stock.

- **Search & Filtering**
    - Filter products by name (partial match), category (multi-select), and availability (in stock or out of stock).
    - Sort products by name, category, price, quantity in stock, and expiration date.
    - Multi-column sorting supported.

- **Pagination**
    - Displays 10 products per page for optimal performance and usability.

- **Inventory Metrics**
    - Total number of in-stock products.
    - Total inventory value.
    - Average price of in-stock products.
    - Metrics shown both overall and by category.

---

## Technical Overview

### Model: `Product`
- `id` (Unique Identifier. Auto-generated)
- `name` (Required, max 120 characters)
- `category` (Required)
- `unitPrice` (Required)
- `expirationDate` (Optional)
- `quantityInStock` (Required)
- `creationDate` (Auto-generated)
- `updateDate` (Auto-generated)

### API Endpoints

| Method | Endpoint                    | Description                                                      |
|--------|-----------------------------|------------------------------------------------------------------|
| GET    | `/products`                 | Retrieve products with pagination support                        |
| POST   | `/products`                 | Create a new product                                             |
| DELETE | `/products`                 | Delete a products                                                |
| PUT    | `/products/{id}`            | Update an existing product                                       |
| PATCH  | `/products/{id}/outofstock` | Mark a product as out of stock                                   |
| PATCH  | `/products/{id}/instock`    | Restore a product's stock<br/>                                   |
| GET    | `/products/summary`         | Retrieves inventory metrics                                      |
| GET    | `/products/filters`         | Retrieves products based on filters, sort methods and pagination |
| GET    | `/products/categories`      | Retrieves all the categories available                           |


### Storage

Currently, product data is stored in a local database using docker. 

---

##  Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Data Storage:** Oracle DB

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Running the Application

```bash
docker run -d \
--name oracle-xe \
-e ORACLE_PASSWORD=admin \
-p 1521:1521 \
-p 5500:5500 \
oracle-xe-inventory-manager:1.0
```
```bash
mvn spring-boot:run
```