package com.ecommerce.spring.common.event.recording.mongo;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventRecorder;

import java.util.List;

public class MongoDomainEventRecorder implements DomainEventRecorder {

    @Override
    public void recordForPublishing(DomainEvent event) {

    }

    @Override
    public void recordForPublishing(List<DomainEvent> events) {

    }

    @Override
    public List<DomainEvent> toBePublishedEvents() {
        return null;
    }

    @Override
    public void markAsPublished(String eventId) {

    }

    @Override
    public void deleteAllPublishingEvents() {

    }

    @Override
    public void increasePublishTry(String eventId) {

    }

    @Override
    public boolean recordForConsuming(DomainEvent event) {
        return false;
    }

    @Override
    public void deleteAllConsumedEvents() {

    }
}
