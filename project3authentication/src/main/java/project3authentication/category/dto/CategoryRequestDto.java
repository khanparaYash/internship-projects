package project3authentication.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "Name is required") String name,
        String description
) {
}
