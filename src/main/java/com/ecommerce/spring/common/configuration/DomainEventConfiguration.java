package com.ecommerce.spring.common.configuration;

import com.ecommerce.shared.event.consume.DomainEventConsumingRecorder;
import com.ecommerce.shared.event.consume.DomainEventConsumingWrapper;
import com.ecommerce.shared.event.publish.DomainEventPublisher;
import com.ecommerce.shared.event.publish.DomainEventPublishingRecorder;
import com.ecommerce.shared.event.publish.DomainEventSender;
import com.ecommerce.shared.utils.DefaultObjectMapper;
import com.ecommerce.shared.utils.DistributedLockExecutor;
import com.ecommerce.spring.common.event.consume.JdbcTemplateDomainEventConsumingRecorder;
import com.ecommerce.spring.common.event.publish.DomainEventBackupPublishScheduler;
import com.ecommerce.spring.common.event.publish.JdbcTemplateDomainEventPublishingRecorder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DomainEventConfiguration {

    @Bean
    public DomainEventPublishingRecorder domainEventPublishingRecorder(NamedParameterJdbcTemplate jdbcTemplate,
                                                                       DefaultObjectMapper objectMapper) {
        return new JdbcTemplateDomainEventPublishingRecorder(jdbcTemplate, objectMapper);
    }


    @Bean
    public DomainEventConsumingRecorder domainEventConsumingRecorder(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcTemplateDomainEventConsumingRecorder(jdbcTemplate);
    }

    @Bean
    public DomainEventPublisher domainEventPublisher(DomainEventPublishingRecorder domainEventPublishingRecorder,
                                                     DistributedLockExecutor distributedLockExecutor,
                                                     DomainEventSender domainEventSender) {
        return new DomainEventPublisher(domainEventPublishingRecorder,
                distributedLockExecutor,
                domainEventSender);
    }

    @Bean
    public DomainEventConsumingWrapper domainEventConsumingWrapper(DomainEventConsumingRecorder domainEventConsumingRecorder) {
        return new DomainEventConsumingWrapper(domainEventConsumingRecorder);
    }

    @Bean
    public DomainEventBackupPublishScheduler domainEventBackupPublishScheduler(DomainEventPublisher publisher) {
        return new DomainEventBackupPublishScheduler(publisher);
    }
}
