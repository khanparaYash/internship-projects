package project3authentication.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDto(
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0") Double price,
        @NotNull(message = "Quantity is required") @Min(value = 0, message = "Quantity must be greater than or equal to 0") Integer quantity,
        @NotNull(message = "Category is required") Long categoryId,
        String imageUrl
) {
}
