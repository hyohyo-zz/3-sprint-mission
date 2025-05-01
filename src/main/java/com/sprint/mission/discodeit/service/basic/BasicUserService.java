package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
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

    public UserResponse create(UserCreateRequest request) {
        // 중복 name, email 검사
        existsByUserName(request.name());
        existsByEmail(request.email());

       UUID profileImageId = null;
       if(request.profileImage() != null && !request.profileImage().isEmpty()) {
           BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(
                   null,
                   request.profileImage().getContentType(),
                   request.profileImage().getBytes(),
                   Instant.now()
           ));

           profileImageId = binaryContent.getId();
       }

       User user = new User(request.name(), request.email(), request.phone(), request.password());
       user.setProfileImageId(profileImageId);
       User savedUser = Objects.requireNonNull(userRepository.create(user));

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
    public List<UserResponse> findByUserName(String name) {
        List<User> users = userRepository.findByUserName(name).orElseThrow();

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

        //4. 프로필 이미지 대체( 있으면 새로 저장)
        if(request.username() != null && ! request.profileImage().isEmpty()) {
            BinaryContent binaryContent = binaryContentRepository.save(request.profileImage().getBytes(),
                    request.profileImage().getOriginalFilename());
            user.setProfileImageId(binaryContent.getId());
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
            throw new IllegalArgumentException(" --해당 유저를 찾을 수 없습니다.");
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("!!유저 탈퇴 실패!! --- 비밀번호 불일치");
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
            throw new IllegalArgumentException(" --- 이미 등록된 이메일입니다.");
        }
    }

    private void existsByUserName(String userName) {
        List<User> users = userRepository.findAll();
        boolean exists = users.stream()
                .anyMatch((u -> u.getName().equals(userName)));
        if(exists) {
            throw new IllegalArgumentException(" --- 이미 등록된 이름입니다.");
        }
    }

    private UserResponse toUserResponse(User user, UserStatus status) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageId(),
                status.isOnline(),
                status
        );
    }
}
