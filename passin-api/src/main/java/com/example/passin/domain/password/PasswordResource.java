package com.example.passin.domain.password;

import com.example.base.BaseResource;
import com.example.passin.message.GetPasswordResponse;
import com.example.passin.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = BaseResource.BASE_URL + PasswordResource.PASSWORD_RESOURCE_URL)
public class PasswordResource {
    public static final String PASSWORD_RESOURCE_URL = "/passwords";

    private final PasswordMapper passwordMapper;
    private final PasswordService passwordService;

    @Inject
    public PasswordResource(PasswordMapper passwordMapper, PasswordService passwordService) {
        this.passwordMapper = passwordMapper;
        this.passwordService = passwordService;
    }

    @PostMapping("/save-password")
    public ResponseEntity<ResponseMessage> savePassword(@RequestBody SavePasswordDto savePasswordDto){
        Password newPassword = new Password();
        newPassword.setHostName(savePasswordDto.getHostName());
        newPassword.setEmail(savePasswordDto.getEmail());
        newPassword.setPassword(savePasswordDto.getPassword());
        newPassword.setUserId(savePasswordDto.getUserId());
        Password addedPassword = addNewPassword(newPassword);

        if(addedPassword.getId() > 0){
            return ResponseEntity.ok().body(new ResponseMessage("Password successfully updated", HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Something went wrong!",HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/get-passwords")
    public List<Password> getAllPassword(@RequestParam(name = "user-id") long id){
        return passwordService.findPasswordByUserId(id);
//        return ResponseEntity.ok().body(new GetPasswordResponse(passwordList,"Password Retrieved Successfully", HttpStatus.OK));
    }

    private Password addNewPassword(Password password) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        password.setCreatedOn(currentTimestamp);
        password.setUpdatedOn(currentTimestamp);
        return passwordService.save(password);
    }
}
