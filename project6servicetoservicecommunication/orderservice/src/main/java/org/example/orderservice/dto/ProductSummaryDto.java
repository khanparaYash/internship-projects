package org.example.orderservice.dto;

public record ProductSummaryDto(
        Long id,
        String name,
        Double price,
        Integer quantity,
        String categoryName,
        String imageUrl
) {
}
