package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String gender;
    private String name;
    private String email;
    private String phone;
    private transient String password;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID profileId;

    public User(String name, String gender, String email, String phone, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함

        this.profileId = null;   // 나중에 등록하는 경우가 대부분??
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User user)) return false;
        return id.equals(user.id);
    }

    public void update(User updateUserData) {
        this.name = updateUserData.name;
        this.gender = updateUserData.gender;
        this.email = updateUserData.email;
        this.phone = updateUserData.phone;
        this.password = updateUserData.password;
        this.updatedAt = Instant.now();

        this.profileId = updateUserData.profileId;
    }

    public String toString() {
        return "User{" +
                "UserName= '" + name + '\'' +
                ", ProfileId= '" + profileId + '\'' +
                ", Gender= '" + gender + '\'' +
                ", email= '" + email + '\'' +
                ", phone= '" + phone + '\'' +
                ", password= '" + password + '\'' +
                '}';
    }
}
