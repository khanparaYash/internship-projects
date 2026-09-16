package in.yashKhanpara.Project_2_Employee_Management.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents a structured API error payload returned to clients.
 *
 * <p>This model includes the timestamp, HTTP status, error summary, message,
 * request path, and optional field-level validation details.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    /**
     * Time when the error was generated.
     */
    private OffsetDateTime timestamp;

    /**
     * HTTP status code associated with the error.
     */
    private int status;

    /**
     * Short HTTP error name such as "Bad Request" or "Not Found".
     */
    private String error;

    /**
     * Human-readable error description.
     */
    private String message;

    /**
     * Request URI that triggered the error.
     */
    private String path;

    /**
     * Optional validation errors keyed by field name.
     */
    private Map<String, String> fieldErrors;

    /**
     * Optional extra details for additional context.
     */
    private List<Object> details;
}
