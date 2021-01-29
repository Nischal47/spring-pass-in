package com.example.passin.ExceptionHandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException{

    private String message;
    private String details;

    public CustomException(){
    }

    public CustomException(String message, String details){
        this.message = message;
        this.details = details;
    }
}
