package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        BinaryContent content = new BinaryContent(
                binaryContent.getUserId(),
                binaryContent.getMessageId(),
                binaryContent.getContent(),
                binaryContent.getContentType(),
                binaryContent.getOriginalFilename()
        );
        data.put(content.getId(), content);

        return content;
    }

    @Override
    public BinaryContent find(UUID id) {
        return data.get(id);
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<BinaryContent> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(binaryContent -> binaryContent.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id) {
        return this.data.remove(id) != null;
    }

    @Override
    public boolean deleteByUserId(UUID userId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .map(BinaryContent::getId)
                .toList();

        boolean deleted = false;
        for (UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }
        return deleted;
    }

    @Override
    public boolean deleteByMessageId(UUID messageId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getMessageId(), messageId))
                .map(BinaryContent::getId)
                .toList();

        boolean deleted = false;
        for (UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }
        return deleted;
    }
}
