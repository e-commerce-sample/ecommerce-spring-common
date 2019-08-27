package com.ecommerce.common.event.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCreatedEvent extends ProductEvent {
    private String name;
    private String description;
    private BigDecimal price;
    private Instant createdAt;

    public ProductCreatedEvent(String productId, String name, String description, BigDecimal price, Instant createdAt) {
        super(productId);
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
