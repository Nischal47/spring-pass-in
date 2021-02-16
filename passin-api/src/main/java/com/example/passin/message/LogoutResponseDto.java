package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class LogoutResponseDto {
    private String responseMessage;
    private HttpStatus httpStatus;

    public LogoutResponseDto(String responseMessage,HttpStatus httpStatus) {
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }
}
