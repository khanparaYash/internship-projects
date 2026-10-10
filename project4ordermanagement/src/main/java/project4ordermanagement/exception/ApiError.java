package project4ordermanagement.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard error response")
public record ApiError(LocalDateTime timestamp, int status, String message, String path) {

}
