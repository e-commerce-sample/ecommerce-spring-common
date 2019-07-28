package com.ecommerce.common.event.publish;

import com.ecommerce.common.logging.AutoNamingLoggerFactory;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RabbitDomainEventBackupPublishScheduler {
    private static final Logger logger = AutoNamingLoggerFactory.getLogger();

    private RabbitDomainEventPublisher publisher;

    public RabbitDomainEventBackupPublishScheduler(RabbitDomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 120000)
    public void run() {
        logger.info("Scheduled trigger domain event publish process.");
        publisher.publish();
    }

}
