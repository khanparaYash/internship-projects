package org.example.orderservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.orderservice.dto.ProductSummaryDto;
import org.example.orderservice.exception.ServiceUnavailableException;
import org.example.orderservice.repository.CatalogClient;
import org.springframework.stereotype.Service;

@Service
public class CatalogClientService {
    private final CatalogClient catalogClient;

    public CatalogClientService(CatalogClient catalogClient) {
        this.catalogClient = catalogClient;
    }

    @Retry(name = "catalogService")
    @CircuitBreaker(name = "catalogService", fallbackMethod = "fallback")
    public ProductSummaryDto getProduct(Long productId) {
        System.out.println("Calling Catalog Service");
        return catalogClient.getProduct(productId);
    }

    public ProductSummaryDto fallback(Long productId, Exception ex) throws Exception {
        if (ex instanceof FeignException.NotFound || ex instanceof FeignException.BadRequest) {
            throw ex;
        }
        throw new ServiceUnavailableException("Catalog service unavailable, please try again");
    }
}
