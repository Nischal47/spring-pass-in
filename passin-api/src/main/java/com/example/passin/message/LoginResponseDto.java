package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private ResponseUserDto user;
    private String message;
    private String token;
    private String refreshToken;

    public LoginResponseDto(ResponseUserDto user, String message, String token,String refreshToken) {
        this.user = user;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public LoginResponseDto() {

    }
}
