package project3authentication.category.dto;

import project3authentication.product.dto.ProductResponseDto;

import java.util.List;

public record CategoryResponseDto(Long id, String name, String description, List<ProductResponseDto> products) {
}
