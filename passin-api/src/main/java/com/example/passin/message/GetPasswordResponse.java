package com.example.passin.message;

import java.util.List;
import com.example.passin.domain.password.Password;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GetPasswordResponse {
    private List<Password> passwordList;
    private String message;
    private HttpStatus httpStatus;

    public GetPasswordResponse(List<Password> passwordList, String message, HttpStatus httpStatus) {
        this.passwordList = passwordList;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
