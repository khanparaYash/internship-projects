package org.example.orderservice.config;

import org.example.orderservice.repository.CatalogClient;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("catalog")
public class CatalogHealthIndicator implements HealthIndicator {
    private final CatalogClient catalogClient;

    public CatalogHealthIndicator(CatalogClient catalogClient) {
        this.catalogClient = catalogClient;
    }

    @Override
    public Health health() {
        try {
            catalogClient.ping();

            return Health.up()
                    .withDetail("catalog-service", "Available")
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("catalog-service", "Unavailable")
                    .build();
        }
    }
}
