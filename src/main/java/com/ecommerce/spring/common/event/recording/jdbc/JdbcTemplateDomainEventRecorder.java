package com.ecommerce.spring.common.event.recording.jdbc;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventRecorder;
import com.ecommerce.shared.jackson.DefaultObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Slf4j
public class JdbcTemplateDomainEventRecorder implements DomainEventRecorder {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DefaultObjectMapper objectMapper;
    private final RowMapper<DomainEvent> mapper;

    public JdbcTemplateDomainEventRecorder(NamedParameterJdbcTemplate jdbcTemplate, DefaultObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.mapper = eventMapper(objectMapper);
    }

    @Override
    public void recordForPublishing(DomainEvent event) {
        recordForPublishing(newArrayList(event));
    }

    @Override
    public void recordForPublishing(List<DomainEvent> events) {
        String sql = "INSERT INTO DOMAIN_EVENT (ID, JSON_CONTENT) VALUES (:id, :json);";

        SqlParameterSource[] parameters = events.stream()
                .map((Function<DomainEvent, SqlParameterSource>) domainEvent ->
                        new MapSqlParameterSource()
                                .addValue("id", domainEvent.getId())
                                .addValue("json", objectMapper.writeValueAsString(domainEvent)))
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, parameters);
    }

    @Override
    public List<DomainEvent> toBePublishedEvents() {
        String sql = "SELECT JSON_CONTENT FROM DOMAIN_EVENT WHERE PUBLISH_TRIES < 5 ORDER BY CREATED_AT LIMIT 50;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public void markAsPublished(String eventId) {
        String sql = "DELETE FROM DOMAIN_EVENT WHERE ID = :id;";
        jdbcTemplate.update(sql, of("id", eventId));
    }

    @Override
    public void deleteAllPublishingEvents() {
        String sql = "DELETE FROM DOMAIN_EVENT;";
        jdbcTemplate.update(sql, newHashMap());
    }

    @Override
    public void increasePublishTry(String eventId) {
        String sql = "UPDATE DOMAIN_EVENT SET PUBLISH_TRIES = PUBLISH_TRIES + 1 WHERE ID = :id;";
        jdbcTemplate.update(sql, ImmutableMap.of("id", eventId));
    }

    @Override
    public boolean recordForConsuming(DomainEvent event) {
        String sql = "INSERT INTO DOMAIN_EVENT_RECEIVE_RECORD (EVENT_ID) VALUES (:eventId);";
        String eventId = event.getId();
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
    public void deleteAllConsumedEvents() {
        String sql = "DELETE FROM DOMAIN_EVENT_RECEIVE_RECORD;";
        jdbcTemplate.update(sql, newHashMap());
    }


    private RowMapper<DomainEvent> eventMapper(DefaultObjectMapper objectMapper) {
        return (rs, rowNum) -> objectMapper.readValue(rs.getString("JSON_CONTENT"), DomainEvent.class);
    }
}

