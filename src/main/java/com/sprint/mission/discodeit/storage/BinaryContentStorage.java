package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  public UUID put(UUID id, byte[] bytes);

  public InputStream get(UUID id);

  public ResponseEntity<?> download(BinaryContentDto binaryContentDto);

}
