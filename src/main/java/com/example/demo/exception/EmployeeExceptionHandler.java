package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class EmployeeExceptionHandler {

    private static String errorMessage = "Exception occurred while processing request {} ";

    @ExceptionHandler({EmployeeException.class})
    public ResponseEntity<EmployeeException> handleStandPlanGeneratorException(final EmployeeException ex) {
        log.error(errorMessage, ex);
        EmployeeException employeeException = new EmployeeException(ex.getErrorCode(), ex.getErrorMessage());
        return ResponseEntity.status(404).body(employeeException);
    }
}
