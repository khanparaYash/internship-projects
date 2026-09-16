package in.yashKhanpara.Project_2_Employee_Management.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Central exception handler for the employee management API.
 *
 * <p>Converts application and validation exceptions into consistent HTTP error responses.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Builds a structured error response payload.
     *
     * @param status the HTTP status to include
     * @param message the error message
     * @param path the request path that caused the error
     * @param fieldErrors validation errors keyed by field name
     * @return an {@link Error} object containing the formatted error details
     */
    private Error build(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        return new Error(OffsetDateTime.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors, null);
    }

    /**
     * Handles validation failures from request body annotations such as {@code @NotBlank} and {@code @Email}.
     *
     * @param ex the validation exception
     * @param req the active HTTP request
     * @return a 400 Bad Request response with field-level validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors()
                .stream().collect(Collectors.toMap(f -> f.getField(), f -> f.getDefaultMessage(), (a, _) -> a));
        Error err = build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Handles parameter-level validation violations such as those triggered by constraints on path variables or query parameters.
     *
     * @param ex the constraint violation exception
     * @param req the active HTTP request
     * @return a 400 Bad Request response containing the violated constraints
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> fields = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage, (a, b) -> a));
        Error err = build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Handles database constraint violations caused by duplicate employee email or phone values.
     *
     * @param ex the persistence exception raised by the database
     * @param req the active HTTP request
     * @return a 409 Conflict response with a clear duplicate-data message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error> handleAlreadyExists(DataIntegrityViolationException ex, HttpServletRequest req) {
        String message = ex.getMostSpecificCause().getMessage();

        if (message.contains("uk_employees_email")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(build(HttpStatus.CONFLICT, "Employee with this email already exists", req.getRequestURI(), null));
        }
        if (message.contains("uk_employees_phone")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(build(HttpStatus.CONFLICT, "Employee with this phone number already exists", req.getRequestURI(), null));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(build(HttpStatus.CONFLICT, "Employee data violates a database constraint", req.getRequestURI(), null));
    }

    /**
     * Handles employee lookup failures when a requested record does not exist.
     *
     * @param ex the not-found exception
     * @param req the active HTTP request
     * @return a 404 Not Found response describing the missing employee
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Error> handleNotFound(EmployeeNotFoundException ex, HttpServletRequest req) {
        Error err = build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    /**
     * Handles malformed request payloads and illegal argument conditions.
     *
     * @param ex the underlying exception
     * @param req the active HTTP request
     * @return a 400 Bad Request response for invalid request data
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<Error> handleBadRequest(Exception ex, HttpServletRequest req) {
        Error err = build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Handles any unclassified application exceptions as a server error.
     *
     * @param ex the unexpected exception
     * @param req the active HTTP request
     * @return a 500 Internal Server Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleAll(Exception ex, HttpServletRequest req) {
        Error err = build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
