package com.example.passin.domain.user;

import com.example.base.BaseService;

public interface UserService extends BaseService<User> {
    boolean validateUser(LoginDto loginDto);
    boolean validateUser(User user,String password);
    boolean validateRefreshToken(RefreshTokenDto refreshToken);
    boolean existByEmail(String email);
    User findByEmail(String email);
    User getUserById(long id);
}
