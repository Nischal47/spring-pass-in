package com.example.passin.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessageDto {
    private String message;

    public ResponseMessageDto(String message) {
        this.message = message;
    }
}
