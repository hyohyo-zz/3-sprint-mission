package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChannelMapper channelMapper;

    //private 채널생성
    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        Channel privateChannel = channelRepository.save(new Channel(ChannelType.PRIVATE));

        List<User> users = userRepository.findAllById(request.participantIds());

        if (users.size() != request.participantIds().size()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID가 포함됨");
        }

        List<ReadStatus> readStatuses = users.stream()
            .map(user -> new ReadStatus(user, privateChannel, privateChannel.getCreatedAt()))
            .toList();

        readStatusRepository.saveAll(readStatuses);
        return channelMapper.toDto(privateChannel);
    }

    //public 채널생성
    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        channelRepository.save(channel);

        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
            .map(channelMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel)
            .map(Channel::getId)
            .toList();

        return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
            .stream()
            .map(channelMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));

        //채널 타입이 private이면 수정 불가
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_PRIVATE_CHANNEL_NOT_UPDATE));
        }
        channel.update(newName, newDescription);

        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND));
        }
        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }
}
