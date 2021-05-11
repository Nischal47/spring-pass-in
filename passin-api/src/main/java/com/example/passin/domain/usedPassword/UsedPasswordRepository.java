package com.example.passin.domain.usedPassword;

import com.example.base.BaseRepository;
import com.example.passin.domain.password.Password;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier(value = "usedPasswordRepository")
public interface UsedPasswordRepository extends BaseRepository<UsedPassword> {
    List<UsedPassword> findUsedPasswordByUserId(long Id);
}
