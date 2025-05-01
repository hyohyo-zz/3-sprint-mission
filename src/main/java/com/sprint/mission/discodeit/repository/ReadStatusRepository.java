package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    public void create(ReadStatus readStatus);

    public ReadStatus find(UUID id);

    public List<ReadStatus> findAll();

    public ReadStatus update(ReadStatus readStatus);

    public ReadStatus delete(UUID id);

    public void deleteByUserId(UUID userId);

    public List<ReadStatus> readByUserId(UUID userId);

    public void deleteByChannelId(UUID channelId);

    public List<ReadStatus> readByChannelId(UUID channelId);

}
