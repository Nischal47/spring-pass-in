package com.example.passin.domain.password;

import com.example.base.BaseResource;
import com.example.passin.domain.usedPassword.UsedPassword;
import com.example.passin.domain.usedPassword.UsedPasswordService;
import com.example.passin.domain.user.User;
import com.example.passin.domain.user.UserService;
import com.example.passin.encryption.Aes;
import com.example.passin.encryption.AesEncryptResponse;
import com.example.passin.encryption.ShaHash;
import com.example.passin.message.DecryptedPasswordResponse;
import com.example.passin.message.GetPasswordResponse;
import com.example.passin.message.RandomPasswordResponseMessage;
import com.example.passin.message.ResponseMessage;
import com.example.passin.passwordGenerator.PasswordGeneratorUtil;
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

    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;
    private final UsedPasswordService usedPasswordService;
    private final Aes aes;
    private final UserService userService;
    private final ShaHash shaHash;
    private final PasswordGeneratorUtil passwordGeneratorUtil;

    @Inject
    public PasswordResource(PasswordEncoder passwordEncoder, PasswordService passwordService, UsedPasswordService usedPasswordService, Aes aes, UserService userService, ShaHash shaHash, PasswordGeneratorUtil passwordGeneratorUtil) {
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
        this.usedPasswordService = usedPasswordService;
        this.aes = aes;
        this.userService = userService;
        this.shaHash = shaHash;
        this.passwordGeneratorUtil = passwordGeneratorUtil;
    }

    @PostMapping("/save-password")
    public ResponseEntity<ResponseMessage> savePassword(@RequestBody SavePasswordDto savePasswordDto) throws Exception {
        boolean isValidUser = validateUser(savePasswordDto.getUserId(), savePasswordDto.getOriginalPassword());
        if (isValidUser) {
            Password newPassword = new Password();
            AesEncryptResponse aesEncryptResponse = encryptPassword(savePasswordDto.getPassword(), savePasswordDto.getOriginalPassword(), savePasswordDto.getUserId());
            newPassword.setHostName(savePasswordDto.getHostName());
            newPassword.setEmail(savePasswordDto.getEmail());
            newPassword.setPassword(new String(aesEncryptResponse.getCipherText(), StandardCharsets.ISO_8859_1));
            newPassword.setIv(aesEncryptResponse.getIv());
            newPassword.setUserId(savePasswordDto.getUserId());
            boolean passwordAlreadyExist = usedPasswordService.doPasswordExist(savePasswordDto.getPassword(), savePasswordDto.getUserId());
            if (!passwordAlreadyExist) {
                Password addedPassword = addNewPassword(newPassword);
                addUsedPassword(savePasswordDto.getUserId(), savePasswordDto.getPassword());
                if (addedPassword.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Password successfully updated", HttpStatus.OK));
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Can't use used Password", HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/update-password")
    public ResponseEntity<ResponseMessage> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        boolean isValidUser = validateUser(updatePasswordDto.getUserId(), updatePasswordDto.getOriginalPassword());
        if (isValidUser) {
            boolean passwordAlreadyExist = usedPasswordService.doPasswordExist(updatePasswordDto.getPassword(), updatePasswordDto.getUserId());
            if (!passwordAlreadyExist) {
                Password password = passwordService.findPasswordById(updatePasswordDto.getPasswordId());
                AesEncryptResponse aesEncryptResponse = encryptPassword(updatePasswordDto.getPassword(), updatePasswordDto.getOriginalPassword(), updatePasswordDto.getUserId());
                updatePassword(password, (new String(aesEncryptResponse.getCipherText(), StandardCharsets.ISO_8859_1)), aesEncryptResponse.getIv());
                addUsedPassword(updatePasswordDto.getUserId(), updatePasswordDto.getPassword());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Password successfully updated", HttpStatus.OK));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Can't use used Password", HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Password does not match", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/get-passwords")
    public ResponseEntity<GetPasswordResponse> getAllPassword(@RequestParam(name = "user-id") long id, @RequestHeader("Authorization") String authorization) {
        List<Password> passwordList = passwordService.findPasswordByUserId(id);
        return ResponseEntity.ok().body(new GetPasswordResponse(passwordList, "Password Retrieved Successfully", HttpStatus.OK));
    }

    @PostMapping("/delete-password")
    public ResponseEntity<ResponseMessage> deletePassword(@RequestBody DeletePasswordDto deletePasswordDto) {
        boolean isValidUser = validateUser(deletePasswordDto.getUserId(), deletePasswordDto.getOriginalPassword());
        if (isValidUser) {
            passwordService.delete(deletePasswordDto.getPasswordId());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Password Deleted Successfully", HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Password does not match", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/generate-random-password")
    public ResponseEntity<RandomPasswordResponseMessage> generateRandomPassword(@RequestParam(name = "minLength") int minLength,@RequestParam(name = "minLength") int maxLength) {
        String randomPassword = passwordGeneratorUtil.generateRandomPassword(minLength, maxLength);
        return ResponseEntity.status(HttpStatus.OK).body(new RandomPasswordResponseMessage("Random Password Generated Successfully", randomPassword, HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/decrypt-password")
    public ResponseEntity<?> decryptPassword(@RequestBody GetPasswordDto getPasswordDto) throws Exception {
        Password password = passwordService.findPasswordById(getPasswordDto.getPasswordId());
        boolean isValidUser = validateUser(password.getUserId(), getPasswordDto.getOriginalPassword());
        if (isValidUser) {
            String decryptedPassword = decryptPassword(password.getPassword(), getPasswordDto.getOriginalPassword(), password.getUserId(), password.getIv());
            PasswordDto passwordDto = new PasswordDto();
            passwordDto.setId(password.getId());
            passwordDto.setHostName(password.getHostName());
            passwordDto.setEmail(password.getEmail());
            passwordDto.setPassword(decryptedPassword);
            passwordDto.setCreatedOn(password.getCreatedOn());
            passwordDto.setUpdatedOn(password.getUpdatedOn());
            return ResponseEntity.ok().body(new DecryptedPasswordResponse("Password has been successfully decrypted", passwordDto, HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Password does not match", HttpStatus.OK));
    }

    private Password addNewPassword(Password password) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        password.setCreatedOn(currentTimestamp);
        password.setUpdatedOn(currentTimestamp);
        return passwordService.save(password);
    }

    public void updatePassword(Password password, String newPassword, byte[] Iv) {
        password.setPassword(newPassword);
        password.setIv(Iv);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        password.setUpdatedOn(currentTimestamp);
        this.passwordService.update(password);
    }

    private AesEncryptResponse encryptPassword(String plainText, String originalPassword, long userId) throws Exception {
        byte[] plainTextByteArray = plainText.getBytes(StandardCharsets.ISO_8859_1);
        byte[] hashedPassword = shaHash.hashBySha(originalPassword);
        User user = userService.getUserById(userId);
        byte[] iv = user.getIv();
        byte[] masterPassword = user.getMasterPassword();
        byte[] masterKey = aes.decrypt(masterPassword, hashedPassword, iv);
        return aes.encrypt(plainTextByteArray, masterKey);
    }

    private String decryptPassword(String cipherText, String originalPassword, long userId, byte[] passwordIv) throws Exception {
        User user = userService.getUserById(userId);
        byte[] hashedPassword = shaHash.hashBySha(originalPassword);
        byte[] iv = user.getIv();
        byte[] masterPassword = user.getMasterPassword();
        byte[] masterKey = aes.decrypt(masterPassword, hashedPassword, iv);
        byte[] plainTextArray = aes.decrypt(cipherText.getBytes(StandardCharsets.ISO_8859_1), masterKey, passwordIv);
        return new String(plainTextArray, StandardCharsets.ISO_8859_1).trim();
    }

    private Optional<String> getJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }
        return Optional.empty();
    }

    private boolean validateUser(long userId, String password) {
        User user = userService.getUserById(userId);
        return userService.validateUser(user, password);
    }

    private void addUsedPassword(long userId, String password) {
        UsedPassword newPassword = new UsedPassword();
        newPassword.setPassword(passwordEncoder.encode(password));
        newPassword.setUserId(userId);
    }

}
