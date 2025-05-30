package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  private final LocalBinaryContentStorage localBinaryContentStorage;

  public BinaryContentMapper(LocalBinaryContentStorage localBinaryContentStorage) {
    this.localBinaryContentStorage = localBinaryContentStorage;
  }

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }
    try (InputStream inputStream = localBinaryContentStorage.get(binaryContent.getId())) {
      return new BinaryContentDto(
          binaryContent.getId(),
          binaryContent.getFileName(),
          binaryContent.getSize(),
          binaryContent.getContentType(),
          inputStream.readAllBytes());
    } catch (IOException e) {
      throw new RuntimeException(
          ErrorMessages.format("BinaryContent", ErrorMessages.ERROR_FILE_READ_FAILED), e);
    }
  }

}
