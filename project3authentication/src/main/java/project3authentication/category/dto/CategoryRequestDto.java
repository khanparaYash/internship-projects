package project3authentication.category.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category creation details")
public record CategoryRequestDto(
        @NotBlank(message = "Name is required") String name,
        String description
) {
}
