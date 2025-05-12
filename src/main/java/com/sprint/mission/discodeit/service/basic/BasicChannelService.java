package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest_public;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    //private 채널생성
    @Override
    public ChannelResponse create(ChannelCreateRequest_private request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.create(channel);

        request.memberIds().stream()
                .map(userId -> new ReadStatus(userId, createdChannel.getId(), Instant.MIN))
                .forEach(readStatusRepository::create);

        return toChannelResponse(createdChannel);
    }

    //public 채널생성
    @Override
    public ChannelResponse create(ChannelCreateRequest_public request) {
       String channelName = request.channelName();
       List<String> categories = request.categories();
       Channel channel = new Channel(ChannelType.PUBLIC, channelName, categories);

       Channel createdChannel = channelRepository.create(channel);

       return toChannelResponse(createdChannel);
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        return channelRepository.find(channelId)
                .map(this::toChannelResponse)
                .orElseThrow(()-> new IllegalArgumentException(ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));
    }

    /*
    * 1. 저장소에서 전체 채널 불러오기
    * 2. 응답리스트 생성
    * 3. 채널 순회 하며 필터링(Private, Public)
    * 4. 반환*/
    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
       List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
               .map(ReadStatus::getChannelId)
               .toList();

       return channelRepository.findAll().stream()
               .filter(channel ->
                       channel.getType().equals(ChannelType.PUBLIC)
               || mySubscribedChannelIds.contains(channel.getId())
               )
               .map(this::toChannelResponse)
               .toList();
    }

    @Override
    public ChannelResponse update(UUID channelId, ChannelUpdateRequest_public request) {
            String newChannelName = request.newChannelName();
            List<String> newCategories = request.newCategories();

            Channel channel = channelRepository.find(channelId)
                    .orElseThrow(()-> new IllegalArgumentException(
                            ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));

            if (channel.getType().equals(ChannelType.PRIVATE)) {
                throw new IllegalArgumentException("private 채널 수정 불가");
            }
            channel.update(newChannelName, newCategories);
            Channel createdChannel = channelRepository.create(channel);

            return toChannelResponse(createdChannel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.find(channelId)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));

        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());

        channelRepository.deleteById(channelId);
    }

    private ChannelResponse toChannelResponse(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> memberIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(memberIds::add);
        }
        return new ChannelResponse(
                channel.getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getCategories(),
                memberIds,
                lastMessageAt
        );
    }
}
