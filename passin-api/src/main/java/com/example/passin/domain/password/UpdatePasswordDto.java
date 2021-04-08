package com.example.passin.domain.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdatePasswordDto {
    private String email;
    private String password;
    private String originalPassword;
    private long userId;
    private long passwordId;
}
