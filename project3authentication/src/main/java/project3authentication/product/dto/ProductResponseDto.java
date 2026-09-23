package project3authentication.product.dto;

public record ProductResponseDto(Long id, String name, Double price, Integer quantity, String categoryName, String imageUrl) {
}
