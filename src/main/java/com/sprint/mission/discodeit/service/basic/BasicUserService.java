package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    public UserResponse create(UserCreateRequest request, Optional<BinaryContentRequest> profileCreateRequest) {
        // 중복 name, email 검사
        existsByUserName(request.name());
        existsByEmail(request.email());

        User user = new User(request.name(), request.email(), request.phone(), request.password());
        User savedUser = userRepository.create(user);

        //프로필 이미지 생성
        if (request.profileImage() != null && request.profileImage().content() != null && request.profileImage().content().length > 0) {
           BinaryContent profileImage = new BinaryContent(
                   savedUser.getId(),
                   null,
                   request.profileImage().content(),
                   request.profileImage().contentType(),
                   request.profileImage().originalFilename()
           );
           BinaryContent savedImage = binaryContentRepository.save(profileImage);
           user.setProfileImageId(savedImage.getId());
           userRepository.update(user);
       }

       UserStatus status = new UserStatus(savedUser.getId(), false);
       userStatusRepository.create(status);

       return toUserResponse(savedUser, status);
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.find(userId).orElseThrow();

        UserStatus status = userStatusRepository.findByUserId(userId)
                .orElse(new UserStatus(userId, false));

        return toUserResponse(user, status);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserStatus status =
                            userStatusRepository.findByUserId(user.getId())
                       .orElse(new UserStatus(user.getId(), false));
                    return toUserResponse(user,status);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findByUserName(String name) {
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)));

        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElse(new UserStatus(user.getId(), false));

        return toUserResponse(user, status);
    }

    @Override
    public UserResponse update(UserUpdateRequest request) {
        //1. 수정할 엔티티 조회
        User user = userRepository.find(request.id()).orElseThrow();

        //2. username/email 변경 시 중복 체크
        if (request.username() != null && !request.username().equals(user.getName())) {
            existsByUserName(request.username());
        }

        if(request.email() != null && !request.email().equals(user.getEmail())) {
            existsByEmail(request.email());
        }

        //3. 변경할 값 있으면 반영
        if(request.username() != null) user.setName(request.username());
        if(request.email() != null ) user.setEmail(request.email());
        if(request.password() != null) user.setPassword(request.password());
        if(request.phone() != null) user.setPhone(request.phone());

        //4. 프로필 이미지 대체( 있으면 새로 저장)
        if (request.profileImage() != null && !request.profileImage().isEmpty()) {
            try {
                BinaryContent binaryContent = new BinaryContent(
                        user.getId(),
                        null,
                        request.profileImage().getBytes(),
                        request.profileImage().getContentType(),
                        request.profileImage().getOriginalFilename()
                );

                binaryContentRepository.save(binaryContent);
                user.setProfileImageId(binaryContent.getId());

            } catch (IOException e) {
                throw new RuntimeException(
                        ErrorMessages.format("ProfileImage", ErrorMessages.ERROR_SAVE));
            }
        }

        //5. 변경된 User 엔티티 저장
        userRepository.create(user);

        //6. UserStatus 조회 ( 없으면 기본값)
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElse(new UserStatus(user.getId(), false));

        //dto로 변환해서 반환
        return toUserResponse(user, status);
    }

    @Override
    public boolean delete(UUID userId, String password) {
        User user = userRepository.find(userId).orElseThrow();
        if (user == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)
            );
        }

        if (!user.getPassword().equals(password)) {
            System.out.println(
                    ErrorMessages.format("User", ErrorMessages.ERROR_MISMATCH));
            return false;
        }

        boolean deleted = userRepository.delete(userId);
        if (deleted) {
            removeUserFromChannels(user);
            binaryContentRepository.deleteByUserId(userId);
            userStatusRepository.deleteByUserId(userId);

            userRepository.delete(userId);
            System.out.println("유저 탈퇴 성공");
        }
        return deleted;
    }

    @Override
    public void removeUserFromChannels(User user) {
        for (Channel channel : channelRepository.findAll()) {
            Set<User> members = new HashSet<>(channel.getMembers());
            if (members.remove(user)) {
                channel.setMembers(members);
                channelRepository.update(channel.getId(), channel);

            }
        }
    }

    private void existsByEmail(String email) {
        List<User> users = userRepository.findAll();
        boolean exists = users.stream()
                .anyMatch((u -> u.getEmail().equals(email)));
        if(exists) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Email", ErrorMessages.ERROR_EXISTS));
        }
    }

    private void existsByUserName(String userName) {
        List<User> users = userRepository.findAll();
        boolean exists = users.stream()
                .anyMatch((u -> u.getName().equals(userName)));
        if(exists) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("UserName", ErrorMessages.ERROR_EXISTS));
        }
    }

    private UserResponse toUserResponse(User user, UserStatus status) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getProfileImageId(),
                user.getProfileImageUrl(),
                status.isOnline()
        );
    }

    public List<UserResponse> findByUserNameKeyWords(String keyword) {
        return userRepository.findAll().stream()
                .filter(user -> user.getName().contains(keyword))
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getId())
                            .orElse(new UserStatus(user.getId(), false));
                    return toUserResponse(user, status);
                })
                .toList();
    }
}
