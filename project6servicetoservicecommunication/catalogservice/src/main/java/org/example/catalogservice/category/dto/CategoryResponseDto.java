package org.example.catalogservice.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.catalogservice.product.dto.ProductResponseDto;

import java.util.List;

@Schema(description = "Category response")
public record CategoryResponseDto(
        Long id,
        String name,
        String description,
        List<ProductResponseDto> products) {
}
