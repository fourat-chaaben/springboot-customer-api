package io.github.fouratchaaben.customerapp.model;

import java.time.LocalDate;
import java.util.UUID;

public class Order {

    private UUID id;
    private UUID customerId;
    private ProductType product;
    private LocalDate orderedOn;

    public Order() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public ProductType getProduct() {
        return product;
    }

    public void setProduct(ProductType product) {
        this.product = product;
    }

    public LocalDate getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDate orderedOn) {
        this.orderedOn = orderedOn;
    }

    public enum ProductType {
        BASIC, PREMIUM, DELUXE
    }

    public static class OrderRequest {
        private UUID customerId;
        private String product;

        public OrderRequest() {
        }

        public UUID getCustomerId() {
            return customerId;
        }

        public void setCustomerId(UUID customerId) {
            this.customerId = customerId;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }
    }
}
