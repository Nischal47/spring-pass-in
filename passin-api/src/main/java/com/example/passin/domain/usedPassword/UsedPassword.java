package com.example.passin.domain.usedPassword;

import com.example.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "used_passwords",schema = "public")
public class UsedPassword extends BaseEntity {

    @Column(name = "passwords")
    private String password;

    @Column(name = "user_id")
    private long userId;
}
