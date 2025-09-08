package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.event.UserLogInOutEvent;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.SseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatusEventListener {

    private final SseService sseService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;

    @EventListener
    @Transactional(readOnly = true)
    public void on(UserLogInOutEvent event) {
        log.info("[SSE] UserLogInOutEvent 수신: userId={}", event.userId());

        userRepository.findById(event.userId())
            .ifPresent(user -> {
                // 1. 유저 상태 업데이트
                UserDto userDto = userMapper.toDto(user);
                sseService.broadcast("users.updated", userDto);
                log.info("[SSE] users.updated 발행: userId={}, online={}",
                    userDto.id(), userDto.online());

                // 2. 개인 채널(DM) 업데이트: 채널마다 따로 발행
                List<ChannelDto> privateChannels = readStatusRepository.findAllByUserId(
                        user.getId())
                    .stream()
                    .map(ReadStatus::getChannel)
                    .filter(c -> c.getType() == ChannelType.PRIVATE)
                    .map(channelMapper::toDto)
                    .toList();

                privateChannels.forEach(ch -> {
                    sseService.broadcast("channels.updated", ch);
                    log.info("[SSE] channels.updated 발행: channelId={}, type={}",
                        ch.id(), ch.type());
                });
            });
    }
}
