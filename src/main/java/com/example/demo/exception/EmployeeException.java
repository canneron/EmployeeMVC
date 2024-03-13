package com.example.demo.exception;

import org.apache.commons.lang3.StringUtils;

public class EmployeeException extends RuntimeException{

    /** errorCode. */
    private final String errorCode;

    /** errorMessage. */
    private final String errorMessage;



    public EmployeeException(final String errorCode, final String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage != null ? errorMessage : StringUtils.EMPTY;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
