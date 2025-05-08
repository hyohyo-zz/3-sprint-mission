package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.create.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    public final UserRepository userRepository;
    public final ChannelRepository channelRepository;
    public final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        User user = userRepository.find(request.userId())
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)
                ));

        Channel channel = channelRepository.find(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)
                ));


        //가장 최근에 읽은 시간 하나만 두기위해 중복 체크
        boolean alreadyExists = readStatusRepository.findByUserId(request.userId()).stream()
                .anyMatch(rs -> rs.getChannelId().equals(request.channelId()));

        if (alreadyExists) {
            throw new IllegalStateException(
                    ErrorMessages.format("Message", ErrorMessages.ERROR_EXISTS)
            );
        }

        ReadStatus readStatus = new ReadStatus(
                request.userId(),
                request.channelId(),
                request.lastReadTime()
        );

        readStatusRepository.create(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        ReadStatus readStatus = readStatusRepository.find(id);
        if (readStatus == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND));
        }
        return readStatus;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.find(request.id());
        if (readStatus == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND)
            );
        }

        readStatus.setLastReadTime(request.newReadTime());
        return readStatusRepository.update(readStatus);
    }

    @Override
    public boolean delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.find(id);
        if (readStatus == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("ReadStatus", ErrorMessages.ERROR_NOT_FOUND)
            );
        }
        readStatusRepository.delete(id);
        return true;
    }
}
