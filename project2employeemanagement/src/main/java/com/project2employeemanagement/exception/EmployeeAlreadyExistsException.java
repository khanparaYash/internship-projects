package com.project2employeemanagement.exception;


public class EmployeeAlreadyExistsException extends RuntimeException {

    public EmployeeAlreadyExistsException(String msg) {
        super(msg);
    }
}
