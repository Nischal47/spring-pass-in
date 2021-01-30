package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private ResponseUserDto user;
    private String message;
    private String token;

    public LoginResponseDto(ResponseUserDto user, String message, String token) {
        this.user = user;
        this.message = message;
        this.token = token;
    }

    public LoginResponseDto() {

    }
}
