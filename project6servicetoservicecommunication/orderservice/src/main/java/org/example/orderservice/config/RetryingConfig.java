package org.example.orderservice.config;


import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;

import org.example.orderservice.exception.ServiceUnavailableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RetryingConfig {
    @Bean
    public RetryConfigCustomizer catalogRetryCustomizer() {
        return RetryConfigCustomizer.of(
                "catalogService",
                builder -> builder.retryOnException(ex -> ex instanceof ServiceUnavailableException fe)
        );
    }
}
