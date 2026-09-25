package project3authentication.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Stable paginated product response")
public record ProductPageResponseDto(
        @Schema(description = "Products in the requested page")
        List<ProductResponseDto> content,
        @Schema(description = "Zero-based page number", example = "0")
        int page,
        @Schema(description = "Number of products requested per page", example = "10")
        int size,
        @Schema(description = "Total number of matching products", example = "42")
        long totalElements,
        @Schema(description = "Total number of available pages", example = "5")
        int totalPages
) {
}
