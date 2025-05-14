package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    public final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        if (request.bytes() == null || request.bytes().length == 0) {
            throw new RuntimeException(
                    ErrorMessages.format("binaryContent", ErrorMessages.ERROR_FILE_UPLOAD_INVALID));
        }

        BinaryContent file = new BinaryContent(
                request.bytes(),
                request.contentType(),
                request.originalFilename()
        );

        return binaryContentRepository.save(file);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.find(id)
                .orElseThrow(() -> new NoSuchElementException(
                            ErrorMessages.format("BinaryContent", ErrorMessages.ERROR_NOT_FOUND)
                ));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this::find)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new IllegalArgumentException(ErrorMessages.format("비밀번호", ErrorMessages.ERROR_MISMATCH));
        }
        binaryContentRepository.deleteById(id);
    }
}
