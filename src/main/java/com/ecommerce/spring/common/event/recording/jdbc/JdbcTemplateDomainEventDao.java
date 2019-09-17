package com.ecommerce.spring.common.event.recording.jdbc;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventDao;
import com.ecommerce.shared.jackson.DefaultObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Slf4j
public class JdbcTemplateDomainEventDao implements DomainEventDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DefaultObjectMapper objectMapper;
    private final RowMapper<DomainEvent> mapper;

    public JdbcTemplateDomainEventDao(NamedParameterJdbcTemplate jdbcTemplate, DefaultObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.mapper = eventMapper(objectMapper);
    }

    @Override
    public void save(List<DomainEvent> events) {
        String sql = "INSERT INTO DOMAIN_EVENT (ID, JSON_CONTENT) VALUES (:id, :json);";

        SqlParameterSource[] parameters = events.stream()
                .map((Function<DomainEvent, SqlParameterSource>) domainEvent ->
                        new MapSqlParameterSource()
                                .addValue("id", domainEvent.get_id())
                                .addValue("json", objectMapper.writeValueAsString(domainEvent)))
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, parameters);

    }

    @Override
    public void delete(String eventId) {
        String sql = "DELETE FROM DOMAIN_EVENT WHERE ID = :id;";
        jdbcTemplate.update(sql, of("id", eventId));
    }

    @Override
    public DomainEvent get(String eventId) {
        String sql = "SELECT JSON_CONTENT FROM DOMAIN_EVENT WHERE ID = :id;";
        return jdbcTemplate.queryForObject(sql, of("id", eventId), mapper);
    }

    @Override
    public List<DomainEvent> nextPublishBatch(int size) {
        String sql = "SELECT JSON_CONTENT FROM DOMAIN_EVENT WHERE STATUS != 'FAILED' ORDER BY CREATED_AT LIMIT :limit;";
        return jdbcTemplate.query(sql, of("limit", size), mapper);
    }

    @Override
    public void markAsPublished(String eventId) {
        delete(eventId);
    }

    @Override
    public void markAsPublishFailed(String eventId) {
        String sql = "UPDATE DOMAIN_EVENT SET STATUS = 'FAILED' WHERE ID = :id;";
        jdbcTemplate.update(sql, of("id", eventId));
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM DOMAIN_EVENT;";
        jdbcTemplate.update(sql, newHashMap());
    }

    private RowMapper<DomainEvent> eventMapper(DefaultObjectMapper objectMapper) {
        return (rs, rowNum) -> objectMapper.readValue(rs.getString("JSON_CONTENT"), DomainEvent.class);
    }
}

