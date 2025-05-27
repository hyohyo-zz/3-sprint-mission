package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity {

  private String username;
  private String email;
  private String password;
  private BinaryContent profile;     // BinaryContent
  private UserStatus userStatus;

  public User(String username, String email, String password, BinaryContent profile,
      UserStatus userStatus) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.userStatus = userStatus;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile, UserStatus newUserStatus) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
    }
    if (newUserStatus != null && !newUserStatus.equals(this.userStatus)) {
      this.userStatus = newUserStatus;
    }
  }
}
