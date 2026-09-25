package project3authentication.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT authentication response")
public record LoginResponseDto(
        @Schema(description = "Signed JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Token type", example = "Bearer")
        String tokenType,
        @Schema(description = "Token lifetime in milliseconds", example = "3600000")
        long expiresIn) {
}
