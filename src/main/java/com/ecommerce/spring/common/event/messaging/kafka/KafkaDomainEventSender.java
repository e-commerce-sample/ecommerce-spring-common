package com.ecommerce.spring.common.event.messaging.kafka;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventSender;

public class KafkaDomainEventSender implements DomainEventSender {

    @Override
    public void send(DomainEvent event) {

    }
}
