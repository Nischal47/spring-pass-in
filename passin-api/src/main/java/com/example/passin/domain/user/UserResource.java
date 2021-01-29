package com.example.passin.domain.user;

import com.example.base.BaseResource;
import com.example.passin.message.SignUpResponseDto;
import com.example.passin.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@Slf4j
@RequestMapping(BaseResource.BASE_URL + UserResource.USER_RESOURCE_URL)
public class UserResource {
    public static final String USER_RESOURCE_URL = "/users";

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserResource(PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, UserService userService, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDto> registerUser(@RequestBody RegisterDto registerInfo){
        if(!userExist(registerInfo.getEmail())){
            User newUser = new User();
            newUser.setEmail(registerInfo.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerInfo.getPassword()));
            newUser.setFirstName(registerInfo.getFirstName());
            newUser.setLastName(registerInfo.getLastName());
            newUser.setDateOfBirth(registerInfo.getDateOfBirth());
            User addedUser = addNewUser(newUser);
            if(addedUser.getId() > 0){
                return ResponseEntity.status(HttpStatus.OK).body(new SignUpResponseDto("Success","Registration Successful",HttpStatus.OK));
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignUpResponseDto("Failed", "Account Already Exists", HttpStatus.CONFLICT));
    }

    public boolean userExist(String email) {
        return userService.existByEmail(email);
    }

    public User addNewUser(User user) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        user.setCreatedOn(currentTimestamp);
        user.setUpdatedOn(currentTimestamp);
        user.setAccountStatus(false);
        user.setLastSeen(currentTimestamp);
        user.setVerifiedUser(false);
        return userService.save(user);
    }
}
