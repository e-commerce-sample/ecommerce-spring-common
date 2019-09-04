package com.ecommerce.spring.common.event.recording.mongo;

import com.ecommerce.shared.event.DomainEventRecorder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDomainEventConfiguration {

    @Bean
    public DomainEventRecorder domainEventPublishingRecorder() {
        return new MongoDomainEventRecorder();
    }

}
