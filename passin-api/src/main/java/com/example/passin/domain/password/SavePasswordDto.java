package com.example.passin.domain.password;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavePasswordDto {
    private String hostName;
    private String email;
    private String password;
    private long userId;
}
