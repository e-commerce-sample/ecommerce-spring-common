package com.ecommerce.spring.common.event;

import com.ecommerce.shared.event.DomainEventConsumingWrapper;
import com.ecommerce.shared.event.DomainEventPublisher;
import com.ecommerce.shared.event.DomainEventRecorder;
import com.ecommerce.shared.event.DomainEventSender;
import com.ecommerce.shared.utils.DistributedLockExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventCommonConfiguration {

    @Bean
    public DomainEventPublisher domainEventPublisher(DomainEventRecorder eventRecorder,
                                                     DistributedLockExecutor lockExecutor,
                                                     DomainEventSender eventSender) {
        return new DomainEventPublisher(eventRecorder,
                lockExecutor,
                eventSender);
    }

    @Bean
    public DomainEventConsumingWrapper domainEventConsumingWrapper(DomainEventRecorder eventRecorder) {
        return new DomainEventConsumingWrapper(eventRecorder);
    }

    @Bean
    public DomainEventConsumingTransactionAdapter domainEventConsumingTransactionAdapter(DomainEventConsumingWrapper wrapper){
        return new DomainEventConsumingTransactionAdapter(wrapper);
    }

    @Bean
    public DomainEventBackupPublishScheduler domainEventBackupPublishScheduler(DomainEventPublisher eventPublisher) {
        return new DomainEventBackupPublishScheduler(eventPublisher);
    }
}
