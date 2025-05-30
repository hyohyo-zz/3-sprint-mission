package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  public Channel save(Channel channel);

  public Optional<Channel> findById(UUID id);

  public List<Channel> findAll();

  public boolean existsById(UUID id);

  public void deleteById(UUID id);

}
