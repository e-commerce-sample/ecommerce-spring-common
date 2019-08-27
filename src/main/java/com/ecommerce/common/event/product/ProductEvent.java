package com.ecommerce.common.event.product;

import com.ecommerce.common.event.DomainEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ProductEvent extends DomainEvent {
    private String productId;

    public ProductEvent(String productId) {
        super();
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
