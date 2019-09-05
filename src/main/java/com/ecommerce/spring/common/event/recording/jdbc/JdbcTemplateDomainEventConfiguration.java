package com.ecommerce.spring.common.event.recording.jdbc;

import com.ecommerce.shared.event.DomainEventRecorder;
import com.ecommerce.shared.jackson.DefaultObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateDomainEventConfiguration {

    @Bean
    public DomainEventRecorder domainEventPublishingRecorder(NamedParameterJdbcTemplate jdbcTemplate,
                                                             DefaultObjectMapper objectMapper) {
        return new JdbcTemplateDomainEventRecorder(jdbcTemplate, objectMapper);
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
