package in.yashKhanpara.Project_2_Employee_Management.exception;

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

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Error build(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        return new Error(OffsetDateTime.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors, null);
    }

    //it's handling the validation exception for example if the name is empty or the email is empty or the phone number is empty
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors()
                .stream().collect(Collectors.toMap(f -> f.getField(), f -> f.getDefaultMessage(), (a, b) -> a));
        Error err = build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    //it's handling the constraint violation exception we use that at parameter level like if we take @PathVariable, and we put @Min or @Max or @NotNull or @NotBlank or @Email
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> fields = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage(), (a, b) -> a));
        Error err = build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    //it's handling the employee already exists exception we use that when we try to create an employee with the same email or phone number
    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<Error> handleAlreadyExists(EmployeeAlreadyExistsException ex, HttpServletRequest req) {
        Error err = build(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    //it's handling the employee not found exception we use that when we try to get an employee by id and the employee is not found
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Error> handleNotFound(EmployeeNotFoundException ex, HttpServletRequest req) {
        Error err = build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    //it's handling the bad request exception we use that when we try to create an employee with invalid datatype like age is string
    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<Error> handleBadRequest(Exception ex, HttpServletRequest req) {
        Error err = build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleAll(Exception ex, HttpServletRequest req) {
        IO.println(ex.getMessage());
        Error err = build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
