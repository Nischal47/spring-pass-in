package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DecryptedPasswordResponse {
    private String plainPassword;
    private HttpStatus httpStatus;

    public DecryptedPasswordResponse(String plainPassword,HttpStatus httpStatus) {
        this.plainPassword = plainPassword;
        this.httpStatus = httpStatus;
    }
}
