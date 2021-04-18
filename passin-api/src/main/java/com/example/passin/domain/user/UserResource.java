package com.example.passin.domain.user;

import com.example.base.BaseResource;
import com.example.passin.encryption.Aes;
import com.example.passin.encryption.AesEncryptResponse;
import com.example.passin.encryption.ShaHash;
import com.example.passin.message.LoginResponseDto;
import com.example.passin.message.LogoutResponseDto;
import com.example.passin.message.RefreshTokenResponseDto;
import com.example.passin.message.ResponseMessage;
import com.example.passin.message.ResponseUserDto;
import com.example.passin.message.SignUpResponseDto;
import com.example.passin.message.TokenValidationResponse;
import com.example.passin.message.ValidTokenDtoResponse;
import com.example.passin.security.JwtTokenService;
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

import javax.inject.Inject;
import java.security.SecureRandom;
import java.sql.Timestamp;

@RestController
@Slf4j
@RequestMapping(BaseResource.BASE_URL + UserResource.USER_RESOURCE_URL)
public class UserResource {
    public static final String USER_RESOURCE_URL = "/users";

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final Aes aes;
    private final ShaHash shaHash;

    @Inject
    public UserResource(PasswordEncoder passwordEncoder, JwtTokenService tokenService, UserService userService, UserMapper userMapper, Aes aes, ShaHash shaHash) {
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.aes = aes;
        this.shaHash = shaHash;
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDto> registerUser(@RequestBody RegisterDto registerInfo) throws Exception {
        if (registerInfo.getEmail().length() == 0 || registerInfo.getPassword().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignUpResponseDto("Failed", "Email and password are mandatory!", HttpStatus.BAD_REQUEST));
        }
        if (!userExists(registerInfo.getEmail())) {
            byte[] randomMasterPassword = generateRandomMasterPassword();
            byte[] hashedPassword = shaHash.hashBySha(registerInfo.getPassword());
            AesEncryptResponse aesEncryptResponse = aes.encrypt(randomMasterPassword,hashedPassword);
            User user = dtoToUser(registerInfo, aesEncryptResponse);
            User savedUser = addNewUser(user);
            if (savedUser.getId() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new SignUpResponseDto("Success", "Registration Successful", HttpStatus.OK));
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignUpResponseDto("Failed", "Account Already Exists!", HttpStatus.CONFLICT));
    }

    private User dtoToUser(RegisterDto registerInfo, AesEncryptResponse aesEncryptResponse) {
        User newUser = new User();
        newUser.setEmail(registerInfo.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerInfo.getEmail() + registerInfo.getPassword()));
        newUser.setFirstName(registerInfo.getFirstName());
        newUser.setLastName(registerInfo.getLastName());
        newUser.setDateOfBirth(registerInfo.getDateOfBirth());
        newUser.setMasterPassword(aesEncryptResponse.getCipherText());
        newUser.setIv(aesEncryptResponse.getIv());
        return newUser;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        boolean isValid = userService.validateUser(loginDto);
        if (isValid) {
            UserDto user = userMapper.toDto(userService.findByEmail(loginDto.getEmail()));
            final String token = tokenService.generateToken(loginDto.getEmail(),user.getId());
            final String refreshToken = tokenService.generateRefreshToken(loginDto.getEmail(),user.getId());
            updateRefreshToken(refreshToken,user.getId());
            return ResponseEntity.ok().body(toLoginResponseDto(user, token, refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDto(null, "Email or password Invalid", null,null));
        }
    }

    private LoginResponseDto toLoginResponseDto(UserDto user, String token, String refreshToken) {
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(user.getId());
        responseUserDto.setEmail(user.getEmail());
        responseUserDto.setFirstName(user.getFirstName());
        responseUserDto.setLastName(user.getLastName());
        responseUserDto.setDateOfBirth(user.getDateOfBirth());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUser(responseUserDto);
        loginResponseDto.setRefreshToken(refreshToken);
        loginResponseDto.setMessage("Success");
        loginResponseDto.setToken(token);
        return loginResponseDto;
    }

    @GetMapping("/token-validate")
    public ResponseEntity<?> validateToken(@RequestParam(name = "token") String token) {
        ValidTokenDtoResponse response = validTokenResponse(token);
        if (response != null) {
            return ResponseEntity.ok().body(new TokenValidationResponse(response, "Token is valid"));
        }
        return ResponseEntity.badRequest().body(new ResponseMessage("Link has expired!!", HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
        boolean isValidRefreshToken = userService.validateRefreshToken(refreshTokenDto);
        if(isValidRefreshToken){
            User user = userService.findByEmail(refreshTokenDto.getEmail());
            final String token = tokenService.generateToken(refreshTokenDto.getEmail(),user.getId());
            return ResponseEntity.ok().body(new RefreshTokenResponseDto(HttpStatus.OK,token));
        }
        return ResponseEntity.badRequest().body(new ResponseMessage("Link has expired!!", HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDto logoutDto){
        boolean isUser = userService.existByEmail(logoutDto.getEmail());
        if(isUser){
            User user = userService.findByEmail(logoutDto.getEmail());
            updateRefreshToken("",user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(new LogoutResponseDto( "Logout Successful", HttpStatus.OK));
        }
        return ResponseEntity.badRequest().body(new ResponseMessage("Logout Failed", HttpStatus.BAD_REQUEST));
    }

    private ValidTokenDtoResponse validTokenResponse(String token) {
        if (token != null && tokenService.validateJwtToken(token)) {
            String email = tokenService.getEmailFromToken(token);
            if (userService.existByEmail(email)) {
                User user = userService.findByEmail(email);
                ValidTokenDtoResponse response = new ValidTokenDtoResponse(user.getEmail());
                return response;
            }
        }
        return null;
    }

    public boolean userExists(String email) {
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

    public void updateRefreshToken(String refreshToken, long userId) {
        User user = userService.getUserById(userId);
        user.setRefreshToken(refreshToken);
        userService.update(user);
    }

    public byte[] generateRandomMasterPassword(){
        byte[] randomMasterPassword = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomMasterPassword);
        return randomMasterPassword;
    }
}
