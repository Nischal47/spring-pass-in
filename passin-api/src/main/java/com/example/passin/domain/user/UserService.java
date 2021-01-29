package com.example.passin.domain.user;

import com.example.base.BaseService;

public interface UserService extends BaseService<User> {
    boolean validateUser(AuthDto authDto);
    boolean existByEmail(String email);
    User findByEmail(String email);
}
