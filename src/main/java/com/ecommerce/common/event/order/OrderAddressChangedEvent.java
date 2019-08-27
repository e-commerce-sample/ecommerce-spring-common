package com.ecommerce.common.event.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAddressChangedEvent extends OrderEvent {
    private String oldAddress;
    private String newAddress;

    public OrderAddressChangedEvent(String orderId, String oldAddress, String newAddress) {
        super(orderId);
        this.oldAddress = oldAddress;
        this.newAddress = newAddress;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public String getNewAddress() {
        return newAddress;
    }
}
