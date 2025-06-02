package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }

    //첫번째 페이지에서 1개만 조회, createdAt 기준 내림차순(가장 최근 메시지)
    Slice<Message> latestMessages = messageRepository.findAllByChannelId(
        channel.getId(), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    Instant lastMessageAt = latestMessages.stream()
        .map(Message::getCreatedAt)
        .findFirst().orElse(Instant.MIN);

    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdWithUser(
        channel.getId());
    List<UserDto> participants = readStatuses.stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants,
        lastMessageAt
    );
  }
}
