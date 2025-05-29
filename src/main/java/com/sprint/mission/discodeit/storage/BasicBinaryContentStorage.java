package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BasicBinaryContentStorage implements BinaryContentStorage {

  private final Path rootDir = Path.of("uploads");

  public BasicBinaryContentStorage() throws Exception {
    Files.createDirectories(rootDir);
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    try {
      Path file = rootDir.resolve(id.toString());
      Files.write(file, bytes);
      return id;
    } catch (IOException e) {
      throw new RuntimeException(ErrorMessages.format(
          "BinaryContent", ErrorMessages.ERROR_FILE_SAVE_FAILED), e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    try {
      Path file = rootDir.resolve(id.toString());
      return Files.newInputStream(file);
    } catch (IOException e) {
      throw new RuntimeException(ErrorMessages.format(
          "BinaryContent", ErrorMessages.ERROR_FILE_READ_FAILED), e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    try {
      InputStream inputStream = get(binaryContentDto.id());
      byte[] fileBytes = inputStream.readAllBytes();

      return ResponseEntity
          .ok()
          .header("Content-Disposition",
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .header("Content-Type", binaryContentDto.contentType())
          .body(fileBytes);
    } catch (IOException e) {
      throw new RuntimeException(ErrorMessages.format(
          "BinaryContent", ErrorMessages.ERROR_FILE_DOWNLOAD_FAILED), e);
    }
  }
}
