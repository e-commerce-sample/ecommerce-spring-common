package com.ecommerce.spring.common.configuration;

import com.ecommerce.shared.event.DomainEventPublisher;
import com.ecommerce.shared.event.DomainEventPublishingRecorder;
import com.ecommerce.shared.event.DomainEventSender;
import com.ecommerce.shared.utils.DistributedLockExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfiguration {

    @Autowired
    private DomainEventPublishingRecorder domainEventPublishingRecorder;

    @Autowired
    private DistributedLockExecutor distributedLockExecutor;

    @Autowired
    private DomainEventSender domainEventSender;

    @Bean
    public DomainEventPublisher domainEventPublisher() {
        return new DomainEventPublisher(domainEventPublishingRecorder,
                distributedLockExecutor,
                domainEventSender);
    }
}
