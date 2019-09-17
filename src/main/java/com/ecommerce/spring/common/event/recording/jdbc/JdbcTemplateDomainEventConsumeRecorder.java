package com.ecommerce.spring.common.event.recording.jdbc;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventConsumeRecorder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Slf4j
public class JdbcTemplateDomainEventConsumeRecorder implements DomainEventConsumeRecorder {

    private final NamedParameterJdbcTemplate jdbcTemplate;


    public JdbcTemplateDomainEventConsumeRecorder(NamedParameterJdbcTemplate jdbcTemplate) {

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
            log.debug("Duplicated key:{}.", eventId);
        } catch (Throwable t) {
            log.debug("Error while record {}.", eventId);
        }
        return false;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM DOMAIN_EVENT_RECEIVE_RECORD;";
        jdbcTemplate.update(sql, newHashMap());

    }
}
