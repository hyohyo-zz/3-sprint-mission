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
    private String name;
    private String email;
    private String phone;
    private transient String password;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID profileImageId;
    private String profileImageUrl;

    public User(String name, String email, String phone, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함

        this.profileImageId = null;   // 나중에 등록하는 경우가 대부분??
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
        this.email = updateUserData.email;
        this.phone = updateUserData.phone;
        this.password = updateUserData.password;
        this.updatedAt = Instant.now();

        this.profileImageId = updateUserData.profileImageId;
        this.profileImageUrl = updateUserData.profileImageUrl;
    }

    public String toString() {
        return "User{" +
                "UserName= '" + name + '\'' +
                ", ProfileId= '" + profileImageId + '\'' +
                ", email= '" + email + '\'' +
                ", phone= '" + phone + '\'' +
                ", password= '" + password + '\'' +
                '}';
    }
}
