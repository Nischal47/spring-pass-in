package com.example.passin.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleException(CustomException e){
        CustomException customException = new CustomException(e.getMessage(),e.getDetails());
        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
