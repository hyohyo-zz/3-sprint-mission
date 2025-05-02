package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    public final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponse create(BinaryContentCreateRequest request) {
        if ((request.userId() == null && request.messageId() == null)
            || request.messageId() == null || request.content().length == 0) {
            throw new IllegalArgumentException("파일 업로드 대상 또는 내용이 잘못되었습니다.");
        }

        BinaryContent file = new BinaryContent(
                request.userId(),
                request.messageId(),
                request.content(),
                request.contentType(),
                request.originalFilename()
        );

        binaryContentRepository.save(file);
        return toBinaryContentResponse(file);
    }

    @Override
    public BinaryContentResponse find(UUID id) {
        BinaryContent file = binaryContentRepository.find(id);
        if (file == null) {
            throw new IllegalArgumentException("해당 Id의 파일이 존재하지 않습니다.");
        }
        return toBinaryContentResponse(file);
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this::find)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public boolean delete(UUID id) {
        binaryContentRepository.delete(id);
        return true;
    }

    private BinaryContentResponse toBinaryContentResponse(BinaryContent file) {
        return new BinaryContentResponse(
                file.getId(),
                file.getContentType(),
                file.getOriginalFilename(),
                file.getUrl()
        );
    }
}
