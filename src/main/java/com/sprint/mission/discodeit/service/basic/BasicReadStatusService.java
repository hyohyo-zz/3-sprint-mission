package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  public final UserRepository userRepository;
  public final ChannelRepository channelRepository;
  public final ReadStatusRepository readStatusRepository;

  @Override
  @Transactional
  public ReadStatus create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("user", ErrorMessages.ERROR_NOT_FOUND)));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("channel", ErrorMessages.ERROR_NOT_FOUND)));

//    boolean exists = readStatusRepository.findAllByUserId(userId).stream()
//        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channelId));

    //쿼리 메서드로 존재 여부 체크 ( 더 굿?)
    if (readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent()) {
      throw new IllegalArgumentException(
          ErrorMessages.format("ReadStatus with user and channel", ErrorMessages.ERROR_EXISTS));
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
        ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND)));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .toList();
  }

  @Override
  @Transactional
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND)));

    readStatus.update(request.newLastReadAt());
    return readStatus;
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    if (!readStatusRepository.existsById(id)) {
      throw new NoSuchElementException(
          ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND));
    }

    readStatusRepository.deleteById(id);
  }
}
