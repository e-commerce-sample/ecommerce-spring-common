package com.ecommerce.spring.common.event.consume;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.consume.DomainEventConsumingRecorder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Slf4j
public class JdbcTemplateDomainEventConsumingRecorder implements DomainEventConsumingRecorder {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateDomainEventConsumingRecorder(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean record(DomainEvent event) {
        String sql = "INSERT INTO DOMAIN_EVENT_RECEIVE_RECORD (EVENT_ID) VALUES (:eventId);";
        String eventId = event.get_id();
        try {
            jdbcTemplate.update(sql, of("eventId", eventId));
            return true;
        } catch (DuplicateKeyException e) {
            log.warn("Duplicated key:{}.", eventId);
        } catch (Throwable t) {
            log.warn("Error while record {}.", eventId);
        }
        return false;
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM DOMAIN_EVENT_RECEIVE_RECORD;";
        jdbcTemplate.update(sql, newHashMap());
    }
}

