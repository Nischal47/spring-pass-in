package com.example.passin.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private ValidTokenDtoResponse response;
    private String message;
}
