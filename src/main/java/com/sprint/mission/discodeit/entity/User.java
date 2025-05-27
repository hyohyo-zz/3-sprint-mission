package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseUpdatableEntity {

  @Column(length = 50, nullable = false, unique = true)
  private String username;

  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Column(length = 60, nullable = false, unique = true)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id", referencedColumnName = "id")
  private BinaryContent profile;     // BinaryContent

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus userStatus;

  public User() {
  }

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
