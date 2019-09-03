package com.ecommerce.spring.common.event.publish;

import com.ecommerce.shared.event.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DomainEventBackupPublishScheduler {

    private DomainEventPublisher publisher;

    public DomainEventBackupPublishScheduler(DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 120000)
    public void run() {
        log.info("Scheduled trigger domain event publish process.");
        publisher.publish();
    }

}
