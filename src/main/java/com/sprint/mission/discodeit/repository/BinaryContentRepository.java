package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    public void save(BinaryContent binaryContent);

    public BinaryContent read(UUID id);

    public List<BinaryContent> readAll();

    public void delete(UUID id);

    //특정 유저의 (프로필 이미지 등) 바이너리 조회
    public List<BinaryContent> readByUserId(UUID userId);\

    public void deleteByUserId(UUID userId);
}
