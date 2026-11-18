package org.example.orderservice.repository;

import org.example.orderservice.dto.ProductSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "catalog-service",
//        url = "http://localhost:8001")
@FeignClient(name = "CATALOGSERVICE")
public interface CatalogClient {
    @GetMapping("/api/v1/api/products/{id}/summary")
    ProductSummaryDto getProduct(
            @PathVariable Long id);

    @GetMapping("/api/v1/api/products/ping")
    String ping();

    @GetMapping("/api/v1/api/products/delay")
    String delay(@RequestParam int ms);

}
