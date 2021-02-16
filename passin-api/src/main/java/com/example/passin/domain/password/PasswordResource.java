package com.example.passin.domain.password;

import com.example.base.BaseResource;
import com.example.passin.domain.user.User;
import com.example.passin.domain.user.UserService;
import com.example.passin.encryption.Aes;
import com.example.passin.encryption.AesEncryptResponse;
import com.example.passin.encryption.ShaHash;
import com.example.passin.message.DecryptedPasswordResponse;
import com.example.passin.message.GetPasswordResponse;
import com.example.passin.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = BaseResource.BASE_URL + PasswordResource.PASSWORD_RESOURCE_URL)
public class PasswordResource {
    public static final String PASSWORD_RESOURCE_URL = "/passwords";

    private final PasswordMapper passwordMapper;
    private final PasswordService passwordService;
    private final Aes aes;
    private final UserService userService;
    private final ShaHash shaHash;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public PasswordResource(PasswordMapper passwordMapper, PasswordService passwordService, Aes aes, UserService userService, ShaHash shaHash, PasswordEncoder passwordEncoder) {
        this.passwordMapper = passwordMapper;
        this.passwordService = passwordService;
        this.aes = aes;
        this.userService = userService;
        this.shaHash = shaHash;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/save-password")
    public ResponseEntity<ResponseMessage> savePassword(@RequestBody SavePasswordDto savePasswordDto) throws Exception {
        Password newPassword = new Password();
        AesEncryptResponse aesEncryptResponse = encryptPassword(savePasswordDto.getPassword(),savePasswordDto.getOriginalPassword(),savePasswordDto.getUserId());
        newPassword.setHostName(savePasswordDto.getHostName());
        newPassword.setEmail(savePasswordDto.getEmail());
        newPassword.setPassword(new String(aesEncryptResponse.getCipherText(),StandardCharsets.ISO_8859_1));
        newPassword.setIv(aesEncryptResponse.getIv());
        newPassword.setUserId(savePasswordDto.getUserId());
        Password addedPassword = addNewPassword(newPassword);

        if(addedPassword.getId() > 0){
            return ResponseEntity.ok().body(new ResponseMessage("Password successfully updated", HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Something went wrong!",HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/get-passwords")
    public ResponseEntity<GetPasswordResponse> getAllPassword(@RequestParam(name = "user-id") long id, @RequestHeader("Authorization") String authorization){
        System.out.println(getJwt(authorization));
        List<Password> passwordList = passwordService.findPasswordByUserId(id);
        return ResponseEntity.ok().body(new GetPasswordResponse(passwordList,"Password Retrieved Successfully", HttpStatus.OK));
    }

    @GetMapping("/decrypt-password")
    public ResponseEntity<DecryptedPasswordResponse> decryptPassword(@RequestBody GetPasswordDto getPasswordDto) throws Exception {
        Password password = passwordService.findPasswordById(getPasswordDto.getPasswordId());
        String decryptedPassword = decryptPassword(password.getPassword(), getPasswordDto.getOriginalPassword(), password.getUserId(),password.getIv());
        return ResponseEntity.ok().body(new DecryptedPasswordResponse(decryptedPassword, HttpStatus.OK));
    }

    private Password addNewPassword(Password password) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        password.setCreatedOn(currentTimestamp);
        password.setUpdatedOn(currentTimestamp);
        return passwordService.save(password);
    }

    private AesEncryptResponse encryptPassword(String plainText, String originalPassword, long userId) throws Exception {
        byte[] plainTextByteArray = plainText.getBytes(StandardCharsets.ISO_8859_1);
        byte[] hashedPassword = shaHash.hashBySha(originalPassword);
        User user = userService.getUserById(userId);
        byte[] iv = user.getIv();
        byte[] masterPassword = user.getMasterPassword();;
        byte[] masterKey = aes.decrypt(masterPassword,hashedPassword,iv);
        AesEncryptResponse aesEncryptResponse = aes.encrypt(plainTextByteArray,masterKey);
        return aesEncryptResponse;
    }

    private String decryptPassword(String cipherText, String originalPassword, long userId, byte[] passwordIv) throws Exception {
        User user = userService.getUserById(userId);
        byte[] hashedPassword = shaHash.hashBySha(originalPassword);
        byte[] iv = user.getIv();
        byte[] masterPassword = user.getMasterPassword();
        byte[] masterKey = aes.decrypt(masterPassword,hashedPassword,iv);
        byte[] plainTextArray = aes.decrypt(cipherText.getBytes(StandardCharsets.ISO_8859_1),masterKey,passwordIv);
        return new String(plainTextArray,StandardCharsets.ISO_8859_1).trim();
    }

    private Optional<String> getJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }
        return Optional.empty();
    }
}
