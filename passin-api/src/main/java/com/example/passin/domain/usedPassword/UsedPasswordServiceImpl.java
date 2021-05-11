package com.example.passin.domain.usedPassword;

import com.example.base.BaseRepository;
import com.example.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UsedPasswordServiceImpl extends BaseServiceImpl<UsedPassword> implements UsedPasswordService {

    UsedPasswordRepository usedPasswordRepository;

    @Inject
    public UsedPasswordServiceImpl(BaseRepository<UsedPassword> repository,UsedPasswordRepository usedPasswordRepository) {
        super(repository);
        this.usedPasswordRepository = usedPasswordRepository;
    }

    @Override
    public List<UsedPassword> findUsedPasswordByUserId(long id) {
        return usedPasswordRepository.findUsedPasswordByUserId(id);
    }
}
