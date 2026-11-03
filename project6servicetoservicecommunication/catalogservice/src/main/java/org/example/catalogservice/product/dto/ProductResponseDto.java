package org.example.catalogservice.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product response")
public record ProductResponseDto(
        Long id,
        String name,
        Double price,
        Integer quantity,
        String categoryName,
        String imageUrl) {
}
