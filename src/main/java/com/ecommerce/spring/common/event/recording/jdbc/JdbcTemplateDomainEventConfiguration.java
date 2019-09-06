package com.ecommerce.spring.common.event.recording.jdbc;

import com.ecommerce.shared.event.DomainEventConsumeRecorder;
import com.ecommerce.shared.event.DomainEventDao;
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
    public DomainEventDao domainEventDao(NamedParameterJdbcTemplate jdbcTemplate,
                                         DefaultObjectMapper objectMapper) {
        return new JdbcTemplateDomainEventDao(jdbcTemplate, objectMapper);
    }

    @Bean
    public DomainEventConsumeRecorder domainEventConsumeRecorder(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcTemplateDomainEventConsumeRecorder(jdbcTemplate);
    }


    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
