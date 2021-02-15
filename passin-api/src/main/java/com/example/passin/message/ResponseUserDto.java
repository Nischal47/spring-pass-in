package com.example.passin.message;

import com.example.base.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ResponseUserDto extends BaseDto {
    private String email;
    private String firstName;
    private String lastName;
    private Timestamp dateOfBirth;
}
