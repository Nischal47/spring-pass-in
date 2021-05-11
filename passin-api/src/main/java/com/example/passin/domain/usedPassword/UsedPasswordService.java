package com.example.passin.domain.usedPassword;

import com.example.base.BaseService;
import com.example.passin.domain.password.Password;

import java.util.List;

public interface UsedPasswordService extends BaseService<UsedPassword> {
    List<UsedPassword> findUsedPasswordByUserId(long id);
}
