package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RefreshTokenResponseDto {
    private String token;
    private HttpStatus httpStatus;

    public RefreshTokenResponseDto(HttpStatus httpStatus,String token) {
        this.token = token;
        this.httpStatus = httpStatus;
    }
}
