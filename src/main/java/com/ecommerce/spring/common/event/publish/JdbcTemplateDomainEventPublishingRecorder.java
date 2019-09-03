package com.ecommerce.spring.common.event.publish;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventPublishingRecorder;
import com.ecommerce.shared.utils.DefaultObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;

@Component
public class JdbcTemplateDomainEventPublishingRecorder implements DomainEventPublishingRecorder {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DefaultObjectMapper objectMapper;
    private final RowMapper<DomainEvent> mapper;

    public JdbcTemplateDomainEventPublishingRecorder(NamedParameterJdbcTemplate jdbcTemplate, DefaultObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.mapper = mapper(objectMapper);
    }

    @Override
    public void record(DomainEvent event) {
        record((newArrayList(event)));
    }

    @Override
    public void record(List<DomainEvent> events) {
        String sql = "INSERT INTO DOMAIN_EVENT (ID, JSON_CONTENT) VALUES (:id, :json);";

        SqlParameterSource[] parameters = events.stream()
                .map((Function<DomainEvent, SqlParameterSource>) domainEvent ->
                        new MapSqlParameterSource()
                                .addValue("id", domainEvent.get_id())
                                .addValue("json", objectMapper.writeValueAsString(domainEvent)))
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, parameters);
    }

    public List<DomainEvent> toBePublishedEvents() {
        String sql = "SELECT JSON_CONTENT FROM DOMAIN_EVENT WHERE PUBLISH_TRIES < 5 ORDER BY CREATED_AT LIMIT 50;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public void delete(String eventId) {
        String sql = "DELETE FROM DOMAIN_EVENT WHERE ID = :id;";
        jdbcTemplate.update(sql, of("id", eventId));
    }

    @Override
    public void increasePublishTry(String eventId) {
        String sql = "UPDATE DOMAIN_EVENT SET PUBLISH_TRIES = PUBLISH_TRIES + 1 WHERE ID = :id;";
        jdbcTemplate.update(sql, ImmutableMap.of("id", eventId));
    }

    private RowMapper<DomainEvent> mapper(DefaultObjectMapper objectMapper) {
        return (rs, rowNum) -> objectMapper.readValue(rs.getString("JSON_CONTENT"), DomainEvent.class);
    }

}
