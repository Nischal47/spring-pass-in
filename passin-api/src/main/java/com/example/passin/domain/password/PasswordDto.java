package com.example.passin.domain.password;

import com.example.base.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PasswordDto extends BaseDto {
    private String hostName;
    private String email;
    private String password;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private long userId;
}
