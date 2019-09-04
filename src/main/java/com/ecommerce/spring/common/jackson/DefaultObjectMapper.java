package com.ecommerce.spring.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.time.ZoneId.of;
import static java.util.TimeZone.getTimeZone;

/**
 * ObjectMapper that converts check exceptions to RuntimeException and customize for domain events
 */
public final class DefaultObjectMapper extends ObjectMapper {

    public DefaultObjectMapper() {
        super();
        findAndRegisterModules()
                .setTimeZone(getTimeZone(of("Asia/Shanghai")))
                .setVisibility(ALL, NONE)
                .setVisibility(FIELD, ANY)
                .registerModule(instantModule())
                .registerModule(trimStringModule())
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        registerModule(new DomainEventJacksonModule());
    }


    private SimpleModule instantModule() {
        return new JavaTimeModule()
                .addSerializer(Instant.class, instantSerializer())
                .addDeserializer(Instant.class, instantDeserializer());
    }


    private SimpleModule trimStringModule() {
        return new SimpleModule()
                .addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
                    @Override
                    public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
                        return jsonParser.getValueAsString().trim();
                    }
                })
                .addSerializer(String.class, new StdScalarSerializer<String>(String.class) {
                    @Override
                    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                        gen.writeString(value.trim());
                    }
                });
    }

    private JsonDeserializer<Instant> instantDeserializer() {
        return new JsonDeserializer<Instant>() {
            @Override
            public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return Instant.ofEpochMilli(p.getValueAsLong());
            }
        };
    }

    private JsonSerializer<Instant> instantSerializer() {
        return new JsonSerializer<Instant>() {
            @Override
            public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.toEpochMilli());
            }
        };
    }

    @Override
    public String writeValueAsString(Object value) {
        try {
            return super.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeValue(Writer w, Object value) {
        try {
            super._configAndWriteValue(_jsonFactory.createGenerator(w), value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return super.readValue(content, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T readValue(String content, TypeReference valueTypeRef) {
        try {
            return super.readValue(content, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
