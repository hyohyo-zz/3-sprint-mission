package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    public UserResponse create(UserCreateRequest request, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        // 중복 name, email 검사
        userRepository.existsByUserName(request.name());
        userRepository.existsByEmail(request.email());

       UUID nullableProfileId = optionalProfileCreateRequest
               .map(profileCreateRequest -> {
                   byte[] bytes = profileCreateRequest.bytes();
                   String contentType = profileCreateRequest.contentType();
                   String fileName = profileCreateRequest.originalFilename();

                   BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName);
                   return binaryContentRepository.save(binaryContent).getId();
               })
               .orElse(null);

       User user = new User(request.name(), request.email(), request.phone(), request.password(), nullableProfileId);
       User savedUser = userRepository.create(user);

       Instant now = Instant.now();
       UserStatus userStatus = new UserStatus(savedUser.getId(), now);
       userStatusRepository.create(userStatus);

       return toUserResponse(savedUser);
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.find(userId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );
        return toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse findByUserName(String name) {
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)));

        return toUserResponse(user);
    }

    @Override
    public UserResponse update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        //1. 수정할 엔티티 조회
        User user = userRepository.find(userId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        //2. username/email 중복 체크
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (userRepository.existsByUserName(newUsername)) {
            throw new IllegalArgumentException("User with username " + newUsername + " already exists");
        }

        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfileImageId())
                            .ifPresent(binaryContentRepository::delete);

                    String fileName = profileRequest.originalFilename();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfileId);

        User savedUser = userRepository.create(user);

        return toUserResponse(savedUser);
    }

    @Override
    public boolean delete(UUID userId, String password) {
        User user = userRepository.find(userId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
        );

        if (!user.getPassword().equals(password)) {
            System.out.println(
                    ErrorMessages.format("User", ErrorMessages.ERROR_MISMATCH));
            return false;
        }

        boolean deleted = userRepository.delete(userId);
        if (deleted) {
//            removeUserFromChannels(user);
            userStatusRepository.deleteByUserId(userId);

            userRepository.delete(userId);
            System.out.println("유저 탈퇴 성공");
        }
        return deleted;
    }
//
//    @Override
//    public void removeUserFromChannels(User user) {
//        for (Channel channel : channelRepository.findAll()) {
//            Set<User> members = new HashSet<>(channel.getMembers());
//            if (members.remove(user)) {
//                channel.setMembers(members);
//                channel.update(channel.getId(), channel);
//            }
//        }
//    }

    private UserResponse toUserResponse(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getCreatedAt(),
                online,
                user.getProfileImageId(),
                user.getProfileImageId() != null
        );
    }

    public List<UserResponse> findByUserNameKeyWords(String keyword) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getName().contains(keyword))
                .map(this::toUserResponse)
                .toList();
    }
}
