package com.example.passin.domain.user;

import com.example.base.BaseServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean validateUser(LoginDto loginDto) {
        User user = userRepository.getUserByEmail(loginDto.getEmail());
        if (user != null) {
            return passwordEncoder.matches(loginDto.getEmail() + loginDto.getPassword(), user.getPassword());
        }
        return false;
    }

    @Override
    public boolean validateUser(User user, String password) {
        if (user != null) {
            return passwordEncoder.matches(user.getEmail() + password, user.getPassword());
        }
        return false;
    }

    @Override
    public boolean validateRefreshToken(RefreshTokenDto refreshToken) {
        User user = userRepository.getUserByEmail(refreshToken.getEmail());
        if (user != null) {
            return refreshToken.getRefreshToken().equals(user.getRefreshToken());
        }
        return false;
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(long id) {
        User user = userRepository.getUserById(id);
        return user;
    }
}
