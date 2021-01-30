package com.example.passin.domain.password;

import com.example.base.BaseService;

import java.util.List;

public interface PasswordService extends BaseService<Password> {
    List<Password> findPasswordByUserId(long id);
}
