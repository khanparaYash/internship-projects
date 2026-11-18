package org.example.catalogservice.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Category creation details")
public record CategoryRequestDto(
        @NotBlank(message = "Name is required") String name,
        String description
) {
}
