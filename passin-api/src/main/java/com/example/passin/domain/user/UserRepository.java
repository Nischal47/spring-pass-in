package com.example.passin.domain.user;

import com.example.base.BaseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "userRepository")
public interface UserRepository extends BaseRepository<User> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getUserById(@Param("id") long id);

    boolean existsUsersByEmail(String email);
}
