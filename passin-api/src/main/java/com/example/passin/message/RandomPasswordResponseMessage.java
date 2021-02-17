package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RandomPasswordResponseMessage {

    private String message;
    private String password;
    private HttpStatus httpStatus;

    public RandomPasswordResponseMessage(String message, String password, HttpStatus httpStatus) {
        this.message = message;
        this.password = password;
        this.httpStatus = httpStatus;
    }
}
