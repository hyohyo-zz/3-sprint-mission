package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    public ReadStatus create(ReadStatus readStatus);

    public Optional<ReadStatus> find(UUID id);

    public List<ReadStatus> findAllByUserId(UUID userId);

    public List<ReadStatus> findAllByChannelId(UUID channelId);

    public boolean existsById(UUID id);

    public void deleteById(UUID id);

    public void deleteAllByChannelId(UUID channelId);



}
