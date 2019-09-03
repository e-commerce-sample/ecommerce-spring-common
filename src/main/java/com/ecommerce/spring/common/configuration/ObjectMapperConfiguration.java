package com.ecommerce.spring.common.configuration;

import com.ecommerce.shared.utils.DefaultObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public DefaultObjectMapper objectMapper() {
        return new DefaultObjectMapper();
    }

}
