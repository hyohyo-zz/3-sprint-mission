package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    public ReadStatus create(ReadStatus readStatus);

    public ReadStatus find(UUID id);

    public List<ReadStatus> findAll();

    public List<ReadStatus> findByUserId(UUID userId);

    public List<ReadStatus> findByChannelId(UUID channelId);

    public boolean delete(UUID id);

    public boolean deleteByUserId(UUID userId);

    public boolean deleteByChannelId(UUID channelId);



}
