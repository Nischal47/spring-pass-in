package com.example.passin.domain.user;

import com.example.base.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "users", schema = "public")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "account_status")
    private Boolean accountStatus;

    @Column(name = "last_seen")
    private Timestamp lastSeen;

    @Column(name = "last_login_ip_address")
    private String lastLoginIpAddress;

    @Column(name = "verified_user")
    private Boolean verifiedUser;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;
}
