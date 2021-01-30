package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseMessage {
    private String message;
    private HttpStatus httpStatus;

    public ResponseMessage(String message,HttpStatus httpStatus){
        this.message= message;
        this.httpStatus = httpStatus;
    }
}
