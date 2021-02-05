package com.example.passin.domain.password;

import com.example.base.BaseRepository;
import java.util.List;

public interface PasswordRepository extends BaseRepository<Password> {
    List<Password> findPasswordByUserId(long id);
}
