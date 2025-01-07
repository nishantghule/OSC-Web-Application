package com.in.exceptions;

import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Resource not found",HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<?> handleStatusRuntimeException(StatusRuntimeException ex) {
        return new ResponseEntity<>("Grpc Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle All Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
