package com.ecommerce.spring.common.event.recording.jdbc;

import com.ecommerce.shared.event.DomainEventRecorder;
import com.ecommerce.spring.common.jackson.DefaultObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcTemplateDomainEventConfiguration {

    @Bean
    public DomainEventRecorder domainEventPublishingRecorder(NamedParameterJdbcTemplate jdbcTemplate,
                                                             DefaultObjectMapper objectMapper) {
        return new JdbcTemplateDomainEventRecorder(jdbcTemplate, objectMapper);
    }

}
