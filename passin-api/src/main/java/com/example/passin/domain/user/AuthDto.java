package com.example.passin.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private String email;
    private String password;
}
