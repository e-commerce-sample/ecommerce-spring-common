package com.ecommerce.common.event;

public enum DomainEventType {
    ORDER_CREATED,
    ORDER_ADDRESS_CHANGED,
    ORDER_PAID,
    ORDER_PRODUCT_CHANGED,
    PRODUCT_CREATED,
    PRODUCT_NAME_UPDATED,
    INVENTORY_CHANGED;

    public String toRoutingKey() {
        return this.name().toLowerCase().replace('_', '.');
    }
}
