package com.example.passin.domain.user;

import com.example.base.BaseEntity;
import liquibase.pro.packaged.C;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "users", schema = "public")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    @Column(name = "email", unique = true)
    public String email;

    @Column(name = "password")
    public String password;

    @Column(name = "created_on")
    public Timestamp createdOn;

    @Column(name = "updated_on")
    public Timestamp updatedOn;

    @Column(name = "account_status")
    public Boolean accountStatus;

    @Column(name = "last_seen")
    public Timestamp lastSeen;

    @Column(name = "last_login_ip_address")
    public String lastLoginIpAddress;

    @Column(name = "verified_user")
    public Boolean verifiedUser;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "avatar")
    public String avatar;

    @Column(name = "date_of_birth")
    public Timestamp dateOfBirth;
}
