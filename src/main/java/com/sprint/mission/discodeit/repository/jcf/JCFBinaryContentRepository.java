package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        BinaryContent content = new BinaryContent(
                binaryContent.getBytes(),
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
    public List<BinaryContent> findAllByIdIn() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        return this.data.remove(id) != null;
    }
}
