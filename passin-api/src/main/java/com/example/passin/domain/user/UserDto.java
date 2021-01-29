package com.example.passin.domain.user;

import com.example.base.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserDto extends BaseDto {
    public String email;
    public String password;
    public Timestamp createdOn;
    public Timestamp updatedOn;
    public Boolean accountStatus;
    public Timestamp lastSeen;
    public String lastLoginIpAddress;
    public Boolean verifiedUser;
    public String firstName;
    public String lastName;
    public String avatar;
    public Timestamp dateOfBirth;
}
