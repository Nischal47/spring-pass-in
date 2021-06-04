package com.example.passin.domain.usedPassword;

import com.example.base.BaseRepository;
import com.example.base.BaseServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UsedPasswordServiceImpl extends BaseServiceImpl<UsedPassword> implements UsedPasswordService {

    UsedPasswordRepository usedPasswordRepository;
    PasswordEncoder passwordEncoder;

    @Inject
    public UsedPasswordServiceImpl(BaseRepository<UsedPassword> repository,UsedPasswordRepository usedPasswordRepository,PasswordEncoder passwordEncoder) {
        super(repository);
        this.usedPasswordRepository = usedPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsedPassword> findUsedPasswordByUserId(long userId) {
        return usedPasswordRepository.findUsedPasswordByUserId(userId);
    }

    @Override
    public boolean doPasswordExist(String newPassword,long userId) {
        List<UsedPassword> usedPassword = findUsedPasswordByUserId(userId);
        for (UsedPassword password : usedPassword) {
            boolean isAlreadyExist = passwordEncoder.matches(newPassword,password.getPassword());
            if(isAlreadyExist){
                return true;
            }
        }
        return false;
    }
}
