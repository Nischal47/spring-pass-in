package com.example.passin.domain.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RandomPasswordGenerationDto {
    private int minLength;
    private int maxLength;
}
