package com.ecommerce.common.event.publish;

import com.ecommerce.sdk.base.DomainEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

import java.io.IOException;
import java.util.Set;


public class DomainEventSerializer extends BeanSerializerBase {

    private static final String TYPE = "_type";

    public DomainEventSerializer(BeanSerializerBase source) {
        super(source);
    }

    private DomainEventSerializer(DomainEventSerializer source,
                                  ObjectIdWriter objectIdWriter) {
        super(source, objectIdWriter);
    }

    private DomainEventSerializer(DomainEventSerializer source,
                                  Set<String> toIgnore) {
        super(source, toIgnore);
    }

    private DomainEventSerializer(DomainEventSerializer domainEventSerializer, ObjectIdWriter objectIdWriter, Object filterId) {
        super(domainEventSerializer, objectIdWriter, filterId);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
        return new DomainEventSerializer(this, objectIdWriter);
    }

    @Override
    protected BeanSerializerBase withIgnorals(Set<String> toIgnore) {
        return new DomainEventSerializer(this, toIgnore);
    }

    @Override
    protected BeanSerializerBase asArraySerializer() {
        return this;
    }

    @Override
    public BeanSerializerBase withFilterId(Object filterId) {
        return new DomainEventSerializer(this, _objectIdWriter, filterId);
    }

    @Override
    public void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        serializeFields(bean, gen, provider);
        if (bean instanceof DomainEvent) {
            gen.writeStringField(TYPE, bean.getClass().getName());
        }
        gen.writeEndObject();
    }
}