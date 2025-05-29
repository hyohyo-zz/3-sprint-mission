package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  public User save(User user);

  public Optional<User> findById(UUID id);

  public Optional<User> findByUsername(String username);

  public List<User> findAll();

  public boolean existsById(UUID id);

  public void deleteById(UUID id);

  public boolean existsByEmail(String email);

  public boolean existsByUsername(String username);

}
