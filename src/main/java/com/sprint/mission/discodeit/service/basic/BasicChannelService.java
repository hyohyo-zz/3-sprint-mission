package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    //private 채널생성
    @Override
    public ChannelResponse createPrivateChannel(ChannelCreateRequest_private request) {
        //creatorId로 유저조회
        User creator = userRepository.find(request.creatorId()).orElseThrow();

        //memberIds로 멤버 조회
        Set<User> members = new HashSet<>();
        for(UUID memberId : request.memberIds()) {
            members.add(findUserById(memberId));
        }

        Channel channel = new Channel(
                "",
                creator,
                new ArrayList<>(),
                members,
                true);

        Channel savedChannel = channelRepository.create(channel);

        for(User member : members) {
            ReadStatus readStatus = new ReadStatus(
                    member.getId(),
                    savedChannel.getId(),
                    Instant.now()
            );
            readStatusRepository.create(readStatus);
        }

        //참여자 id추출
        List<UUID> memberIds = readStatusRepository.findByChannelId(savedChannel.getId()).stream()
                .map(ReadStatus::getUserId).toList();

        return toChannelResponse(savedChannel, lastMessageTime(savedChannel.getId()), memberIds);
    }

    //public 채널생성
    @Override
    public ChannelResponse createPublicChannel(ChannelCreateRequest_public request) {
        User creator = userRepository.find(request.creatorId()).orElseThrow();

        Set<User> members = new HashSet<>();
        for(UUID memberId : request.memberIds()) {
            members.add(findUserById(memberId));
        }

        Channel channel = new Channel(
                request.channelName(),
                creator,
                request.categories(),
                members,
                false
        );

        validateDuplicateChannelName(channel);
        channel.validateUniqueCategory();
        channel.addCreatorToMembers();

        Channel savedChannel = channelRepository.create(channel);

        return toChannelResponse(savedChannel, lastMessageTime(savedChannel.getId()), List.of());
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        Channel channel = channelRepository.find(channelId).orElseThrow();
        if (channel == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND));
        }

        List<UUID> memberIds = new ArrayList<>();
        if (channel.isPrivate()) {
            List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
            for (ReadStatus readStatus : readStatuses) {
                memberIds.add(readStatus.getUserId());
            }
        }

        return toChannelResponse(channel, lastMessageTime(channel.getId()), memberIds);
    }

    /*
    * 1. 저장소에서 전체 채널 불러오기
    * 2. 응답리스트 생성
    * 3. 채널 순회 하며 필터링(Private, Public)
    * 4. 반환*/
    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
       List<Channel> channels = channelRepository.findAll();
       List<ChannelResponse> responses = new ArrayList<>();

       for (Channel channel : channels) {
           if(channel.isPrivate()) {
               processPrivateChannel(channel, userId, responses);
           } else {
               processPublicChannel(channel, responses);
           }
       }
       return responses;
    }

    @Override
    public List<ChannelResponse> findByChannelName(String channelName) {
        List<Channel> channels = channelRepository.findByChannelName(channelName);
        if (channels.isEmpty()) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND));

        }

        List<ChannelResponse> responses = new ArrayList<>();

        for (Channel channel : channels) {
            List<UUID> memberIds = channel.isPrivate()
                    ? readStatusRepository.findByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId).toList()
                    :List.of();
            if (channel.isPrivate()) {
                List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channel.getId());
                for (ReadStatus readStatus : readStatuses) {
                    memberIds.add(readStatus.getUserId());
                }
            }
            responses.add(toChannelResponse(channel, lastMessageTime(channel.getId()), memberIds));
        }
        return responses;
    }

    @Override
    public ChannelResponse update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.find(request.channelId()).orElseThrow();
        if (channel == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND));
        }
        if(channel.isPrivate()) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Channel", ErrorMessages.ERROR_PRIVATE_CHANNEL_NOT_UPDATE));
        }

        channel.update(request.newChannelName(), request.newCategories());
        User newCreator = findUserById(request.newKeyUserId());
        channel.setCreator(newCreator);

        List<UUID> memberIds = List.of();

        return toChannelResponse(channel, lastMessageTime(channel.getId()), memberIds);
    }

    @Override
    public boolean delete(UUID id, UUID userId, String password) {
        Channel channel = channelRepository.find(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));

        if (!channel.getCreator().getPassword().equals(password)) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("password", ErrorMessages.ERROR_MISMATCH)
            );
        }

        messageRepository.deleteByChannelId(id);
        readStatusRepository.deleteByChannelId(id);

        return channelRepository.delete(id, userId, password);
    }

    @Override
    public Set<User> members(UUID id) {
        return channelRepository.members(id);
    }

    private void validateDuplicateChannelName(Channel channel) {
        List<Channel> channels = channelRepository.findByChannelName(channel.getChannelName());
        if (channels.stream().anyMatch(c -> c.getCreator().equals(channel.getCreator())
                && c.getChannelName().equals(channel.getChannelName()))) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND));
        }
    }

    private User findUserById(UUID userId) {
        User user = userRepository.find(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)));
        return user;
    }

    private Instant lastMessageTime(UUID channelId) {
        return messageRepository.findLastMessageTimeByChannelId(channelId)
                .orElse(null);
    }

    private void processPrivateChannel(Channel channel, UUID userId, List<ChannelResponse> responses) {
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channel.getId());

        // 해당 유저가 참여자인지
        boolean isMember = readStatuses.stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId));

        //아니라면 조회 대상제외
        if(!isMember) return;

        //참여자 id리스트
        List<UUID> memberIds = readStatuses.stream()
                .map(ReadStatus::getUserId)
                .toList();

        responses.add(toChannelResponse(channel, lastMessageTime(channel.getId()), memberIds));
    }

    public void processPublicChannel(Channel channel, List<ChannelResponse> responses) {
        // Public 채널은 멤버 리스트 없이 응답
        responses.add(toChannelResponse(channel, lastMessageTime(channel.getId()), List.of()));
    }

    private ChannelResponse toChannelResponse(Channel channel, Instant lastMessageTime, List<UUID> memberIds) {
        return new ChannelResponse(
                channel.getId(),
                channel.getChannelName(),
                channel.getCategories(),
                channel.isPrivate(),
                lastMessageTime,
                memberIds,
                channel.getCreator().getId()
        );
    }
}
