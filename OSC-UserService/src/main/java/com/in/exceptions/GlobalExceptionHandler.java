package com.in.exceptions;

import com.in.dtos.Response;
import com.in.dtos.ResponseCode;
import com.in.mappers.StatusCodes;
import io.grpc.StatusRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    //for date format validation
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateFormat(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("dateOfBirth", "Invalid date format, please use 'yyyy-MM-dd'");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Response> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Response response = new Response(StatusCodes.EMAIL_ALREADY_EXISTS, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //common exception handler for user not found(199) and incorrect user id (201)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        if (ex.getStatusCode() == 199) {
            // Return ResponseCode only
            Response response = new Response(StatusCodes.USER_ID_NOT_FOUND, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            ResponseCode response = new ResponseCode(StatusCodes.USERID_INCORRECT);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Handle All Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGenericException(Exception ex) {
        Response response = new Response(StatusCodes.UNEXPECTED_ERROR, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Response> handleStatusRuntimeException(StatusRuntimeException ex) {
        if (ex.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
            Response response = new Response(StatusCodes.USERID_INCORRECT, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Response response = new Response(StatusCodes.UNEXPECTED_ERROR, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



