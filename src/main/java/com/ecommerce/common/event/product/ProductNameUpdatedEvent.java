package com.ecommerce.common.event.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductNameUpdatedEvent extends ProductEvent {
    private String oldName;
    private String newName;

    public ProductNameUpdatedEvent(String productId, String oldName, String newName) {
        super(productId);
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }
}
