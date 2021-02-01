package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SignUpResponseDto {
    private String response;
    private String message;
    private HttpStatus httpStatus;

    public SignUpResponseDto(String response, String message, HttpStatus httpStatus) {
        this.response = response;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
