package com.example.passin.domain.usedPassword;

import com.example.base.BaseService;

import java.util.List;

public interface UsedPasswordService extends BaseService<UsedPassword> {
    List<UsedPassword> findUsedPasswordByUserId(long userId);
    boolean doPasswordExist(String newPassword,long userId);
}
