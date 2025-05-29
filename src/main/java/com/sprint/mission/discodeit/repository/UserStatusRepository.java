package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  public UserStatus save(UserStatus userStatus);

  public Optional<UserStatus> findById(UUID id);

  public Optional<UserStatus> findByUserId(UUID userId);

  public List<UserStatus> findAll();

  public boolean existsById(UUID id);

  public void deleteById(UUID id);

  public void deleteByUserId(UUID userId);
}
