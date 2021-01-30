package com.example.passin.domain.password;

import com.example.base.BaseResource;
import com.example.passin.message.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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

    @GetMapping("/hello")
    public ResponseEntity<ResponseMessage> hello(){
        return ResponseEntity.ok().body(new ResponseMessage("Hello"));
    }
}
