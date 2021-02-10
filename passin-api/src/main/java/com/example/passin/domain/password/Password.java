package com.example.passin.domain.password;

import com.example.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "passwords",schema = "public")
public class Password extends BaseEntity {
    @Column(name = "host_name",unique = true)
    private String hostName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "iv")
    private byte[] iv;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "user_id")
    private long userId;
}
