package com.ecommerce.spring.common.event;

import com.ecommerce.shared.event.publish.DomainEventPublishingRecorder;
import com.ecommerce.shared.model.BaseAggregate;
import com.ecommerce.shared.model.BaseRepository;

public abstract class JdbcTemplateRepository<AR extends BaseAggregate> extends BaseRepository {

    public JdbcTemplateRepository(DomainEventPublishingRecorder recorder) {
        super(recorder);
    }
}
