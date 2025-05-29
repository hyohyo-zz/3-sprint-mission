package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  public final BinaryContentRepository binaryContentRepository;
  public final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    byte[] bytes = request.bytes();
    if (bytes == null || bytes.length == 0) {
      throw new RuntimeException(
          ErrorMessages.format("binaryContent", ErrorMessages.ERROR_FILE_UPLOAD_INVALID));
    }

    //1. 메타데이터만 가진 BinaryContent 객체 생성
    BinaryContent file = new BinaryContent(
        request.fileName(),
        (long) bytes.length,
        request.contentType()
    );

    //2. DB 저장
    BinaryContent savedFile = binaryContentRepository.save(file);

    //3. 실제 바이너리 데이터 저장소에 따로 저장
    binaryContentStorage.put(savedFile.getId(), bytes);

    return binaryContentRepository.save(file);
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
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
      throw new IllegalArgumentException(
          ErrorMessages.format("binaryContent", ErrorMessages.ERROR_EXISTS));
    }
    binaryContentRepository.deleteById(id);
  }
}
