package com.example.passin.domain.password;

import com.example.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class PasswordServiceImpl extends BaseServiceImpl<Password> implements PasswordService{
    PasswordRepository passwordRepository;

    @Inject
    public PasswordServiceImpl(PasswordRepository passwordRepository) {
        super(passwordRepository);
        this.passwordRepository = passwordRepository;
    }

    @Override
    public List<Password> findPasswordByUserId(long id) {
        return passwordRepository.findPasswordByUserId(id);
    }

    @Override
    public Password findPasswordById(long id) {
        return passwordRepository.findPasswordById(id);
    }
}
