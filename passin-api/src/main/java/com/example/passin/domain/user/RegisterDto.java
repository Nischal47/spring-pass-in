package com.example.passin.domain.user;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class RegisterDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Timestamp dateOfBirth;
}
