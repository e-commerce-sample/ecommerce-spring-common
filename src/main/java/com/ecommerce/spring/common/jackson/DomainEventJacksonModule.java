package com.ecommerce.spring.common.jackson;

import com.ecommerce.shared.event.DomainEvent;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class DomainEventJacksonModule extends SimpleModule {
    static final String TYPE = "_type";

    public DomainEventJacksonModule() {
        super();
        super.addDeserializer(DomainEvent.class, new DomainEventJacksonDeserializer(DomainEvent.class));
    }

    public void setupModule(SetupContext context) {
        super.setupModule(context);

        context.addBeanSerializerModifier(new BeanSerializerModifier() {

            public JsonSerializer<?> modifySerializer(
                    SerializationConfig config,
                    BeanDescription beanDesc,
                    JsonSerializer<?> serializer) {
                if (serializer instanceof BeanSerializerBase) {
                    return new DomainEventJacksonSerializer((BeanSerializerBase) serializer);
                }
                return serializer;

            }
        });
    }
}
