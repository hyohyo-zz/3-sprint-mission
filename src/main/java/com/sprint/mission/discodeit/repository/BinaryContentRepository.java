package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    public BinaryContent save(BinaryContent binaryContent);

    public BinaryContent find(UUID id);

    public List<BinaryContent> findAllByIdIn();

    public boolean delete(UUID id);

}
