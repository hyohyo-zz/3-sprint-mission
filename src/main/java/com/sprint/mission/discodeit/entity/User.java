package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String gender;
    private String name;
    private String email;
    private String phone;
    private transient String password;
    private long createdAt;
    private long updatedAt;

    public User(String name, String gender, String email, String phone, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void update(User updateUserData) {
        this.name = updateUserData.name;
        this.gender = updateUserData.gender;
        this.email = updateUserData.email;
        this.phone = updateUserData.phone;
        this.password = updateUserData.password;
        this.updatedAt = System.currentTimeMillis();
    }

    public String toString() {
        return "User{" +
                "UserName= '" + name + '\'' +
                ", Gender= '" + gender + '\'' +
                ", email= '" + email + '\'' +
                ", phone= '" + phone + '\'' +
                ", password= '" + password + '\'' +
                '}';
    }
}
