package in.yashKhanpara.Project_2_Employee_Management.exception;


public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String msg) {
        super(msg);
    }
}
