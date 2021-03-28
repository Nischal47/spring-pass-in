package com.example.passin.message;

import com.example.passin.domain.password.PasswordDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DecryptedPasswordResponse {
    private String message;
    private PasswordDto decryptedPassword;
    private HttpStatus httpStatus;

    public DecryptedPasswordResponse(String message, PasswordDto passwordDto,HttpStatus httpStatus) {
        this.message = message;
        this.decryptedPassword = passwordDto;
        this.httpStatus = httpStatus;
    }
}
