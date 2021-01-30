package com.example.passin.domain.user;

import com.example.base.BaseResource;
import com.example.passin.message.LoginResponseDto;
import com.example.passin.message.ResponseMessage;
import com.example.passin.message.ResponseUserDto;
import com.example.passin.message.SignUpResponseDto;
import com.example.passin.message.TokenValidationResponse;
import com.example.passin.message.ValidTokenDtoResponse;
import com.example.passin.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

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
        if(registerInfo.getEmail().length() == 0 || registerInfo.getPassword().length() == 0 ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignUpResponseDto("Failed","Email and password are mandatory!",HttpStatus.BAD_REQUEST));
        }
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
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignUpResponseDto("Failed", "Account Already Exists!", HttpStatus.CONFLICT));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        boolean isValid = userService.validateUser(loginDto);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        if (isValid) {
            UserDto user = userMapper.toDto(userService.findByEmail(loginDto.getEmail()));
            ResponseUserDto responseUserDto = new ResponseUserDto();
            responseUserDto.setId(user.getId());
            responseUserDto.setEmail(user.getEmail());
            loginResponseDto.setUser(responseUserDto);
            final String token = jwtTokenUtil.generateToken(loginDto.getEmail());
            loginResponseDto.setMessage("Success");
            loginResponseDto.setToken(token);
            return ResponseEntity.ok().body(loginResponseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDto(null, "Email or password Invalid", null));
        }
    }

    @GetMapping("/token-validate")
    public ResponseEntity<?> validateToken(@RequestParam(name = "token") String token) {
        System.out.println(token);
        ValidTokenDtoResponse response = validTokenResponse(token);
        if (response != null) {
            return ResponseEntity.ok().body(new TokenValidationResponse(response, "Token is valid"));
        }
        return ResponseEntity.badRequest().body(new ResponseMessage("Link has expired!!"));
    }

    private ValidTokenDtoResponse validTokenResponse(String token) {
        if (token != null && jwtTokenUtil.validateJwtToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            if (userService.existByEmail(email)) {
                User user = userService.findByEmail(email);
                ValidTokenDtoResponse response = new ValidTokenDtoResponse(user.getEmail());
                return response;
            }
        }
        return null;
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
