package com.example.passin.domain.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPasswordDto {
    private long passwordId;
    private String originalPassword;
}
