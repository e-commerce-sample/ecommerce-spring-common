package com.ecommerce.common.event.publish;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RabbitDomainEventBackupPublishScheduler {
    private RabbitDomainEventPublisher publisher;

    public RabbitDomainEventBackupPublishScheduler(RabbitDomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 60000)
    public void run() {
        publisher.publish();
    }

}
