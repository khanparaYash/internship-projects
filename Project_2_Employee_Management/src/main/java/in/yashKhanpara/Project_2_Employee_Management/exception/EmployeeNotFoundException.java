package in.yashKhanpara.Project_2_Employee_Management.exception;

/**
 * Thrown when an employee lookup or modification request references an ID that does not exist.
 */
public class EmployeeNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the details of the missing employee.
     *
     * @param message the descriptive error message
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
