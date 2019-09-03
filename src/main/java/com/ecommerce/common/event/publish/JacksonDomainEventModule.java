package com.ecommerce.common.event.publish;

import com.ecommerce.sdk.base.DomainEvent;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class JacksonDomainEventModule extends SimpleModule {

    public JacksonDomainEventModule() {
        super();
        super.addDeserializer(DomainEvent.class, new DomainEventDeserializer(DomainEvent.class));
    }

    public void setupModule(SetupContext context) {
        super.setupModule(context);

        context.addBeanSerializerModifier(new BeanSerializerModifier() {

            public JsonSerializer<?> modifySerializer(
                    SerializationConfig config,
                    BeanDescription beanDesc,
                    JsonSerializer<?> serializer) {
                if (serializer instanceof BeanSerializerBase) {
                    return new DomainEventSerializer((BeanSerializerBase) serializer);
                }
                return serializer;

            }
        });
    }
}
