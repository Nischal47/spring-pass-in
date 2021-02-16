package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RefreshTokenResponseDto {
    private String refreshToken;
    private HttpStatus httpStatus;

    public RefreshTokenResponseDto(HttpStatus httpStatus,String refreshToken) {
        this.refreshToken = refreshToken;
        this.httpStatus = httpStatus;
    }
}
