package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.create.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    public final UserRepository userRepository;
    public final ChannelRepository channelRepository;
    public final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException(ErrorMessages.format("user", ErrorMessages.ERROR_NOT_FOUND));
        }
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException(ErrorMessages.format("channel", ErrorMessages.ERROR_NOT_FOUND));
        }
        if (readStatusRepository.findAllByUserId(userId).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            throw new IllegalArgumentException(ErrorMessages.format("ReadStatus with user and channel", ErrorMessages.ERROR_EXISTS));
        }

        Instant lastReadTime = request.lastReadTime();
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadTime);
        return readStatusRepository.create(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.find(id).orElseThrow(()-> new NoSuchElementException(
                ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND)));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .toList();
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.find(readStatusId).orElseThrow(()-> new NoSuchElementException(
                ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND)));

        readStatus.update(request.newReadTime());
        return readStatusRepository.create(readStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new NoSuchElementException(
                    ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND));
        };
        readStatusRepository.deleteById(id);
    }
}
