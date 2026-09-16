package in.yashKhanpara.Project_2_Employee_Management.exception;

/**
 * Thrown when an employee cannot be created because the supplied email or phone
 * value conflicts with an existing employee record.
 */
public class EmployeeAlreadyExistsException extends RuntimeException {

    /**
     * Creates the exception with a descriptive message.
     *
     * @param msg the reason the employee already exists
     */
    public EmployeeAlreadyExistsException(String msg) {
        super(msg);
    }
}
