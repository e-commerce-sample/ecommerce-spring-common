package com.ecommerce.common.event.consume;

import com.ecommerce.sdk.base.DomainEvent;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Component
public class DomainEventRecordDao {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public DomainEventRecordDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void recordEvent(DomainEvent event) {
        String sql = "INSERT INTO DOMAIN_EVENT_RECEIVE_RECORD (EVENT_ID) VALUES (:eventId);";
        jdbcTemplate.update(sql, of("eventId", event.get_id()));
    }

    public void clear() {
        String sql = "DELETE FROM DOMAIN_EVENT_RECEIVE_RECORD;";
        jdbcTemplate.update(sql, newHashMap());
    }
}

