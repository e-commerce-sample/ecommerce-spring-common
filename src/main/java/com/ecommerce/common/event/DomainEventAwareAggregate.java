package com.ecommerce.common.event;

import com.ecommerce.sdk.base.DomainEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class DomainEventAwareAggregate {
    @JsonIgnore
    private final List<DomainEvent> events = newArrayList();

    protected void raiseEvent(DomainEvent event) {
        this.events.add(event);
    }

    void clearEvents() {
        this.events.clear();
    }

    List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
