package com.ecommerce.spring.common.event;

import com.ecommerce.shared.event.DomainEventConsumeRecorder;
import com.ecommerce.shared.event.DomainEventDao;
import com.ecommerce.shared.event.DomainEventPublisher;
import com.ecommerce.shared.event.DomainEventSender;
import com.ecommerce.shared.utils.DistributedLockExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventCommonConfiguration {

    @Bean
    public DomainEventPublisher domainEventPublisher(DomainEventDao eventDao,
                                                     DistributedLockExecutor lockExecutor,
                                                     DomainEventSender eventSender) {
        return new DefaultDomainEventPublisher(eventDao,
                lockExecutor,
                eventSender);
    }

    @Bean
    public DomainEventBackupPublishScheduler domainEventBackupPublishScheduler(DomainEventPublisher eventPublisher) {
        return new DomainEventBackupPublishScheduler(eventPublisher);
    }

    @Bean
    public DomainEventConsumeWrapper domainEventConsumingWrapper(DomainEventConsumeRecorder eventRecorder) {
        return new DomainEventConsumeWrapper(eventRecorder);
    }

}
