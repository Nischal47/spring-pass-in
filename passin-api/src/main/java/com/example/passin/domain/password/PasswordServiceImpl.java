package com.example.passin.domain.password;

import com.example.base.BaseRepository;
import com.example.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PasswordServiceImpl extends BaseServiceImpl<Password> implements PasswordService{

    @Inject
    public PasswordServiceImpl(BaseRepository<Password> repository) {
        super(repository);
    }
}
