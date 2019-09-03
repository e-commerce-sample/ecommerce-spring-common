package com.ecommerce.spring.common.event.consume;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventConsumingRecorder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Component
public class JdbcTemplateDomainEventRecorder implements DomainEventConsumingRecorder {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateDomainEventRecorder(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void record(DomainEvent event) {
        String sql = "INSERT INTO DOMAIN_EVENT_RECEIVE_RECORD (EVENT_ID) VALUES (:eventId);";
        jdbcTemplate.update(sql, of("eventId", event.get_id()));
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM DOMAIN_EVENT_RECEIVE_RECORD;";
        jdbcTemplate.update(sql, newHashMap());
    }
}

