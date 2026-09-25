package project3authentication.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User login credentials")
public record LoginRequestDto(
        @Schema(description = "Registered email address", example = "user@example.com")
        String email,
        @Schema(description = "Account password", example = "secret123", format = "password")
        String password) {
}
