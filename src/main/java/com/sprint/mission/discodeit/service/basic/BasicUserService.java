package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final ChannelRepository channelRepository;

    private final UserRepository userRepository;

    private final UserStatusRepository userStatusRepository;


    public UserDto create(UserCreateRequest request) {
        validateDuplicateNameAndEmail(request);

        User user = new User(request.name(), request.gender(), request.email(), request.phone(), request.password());

        if(request.profileImageId() != null) {
            user.setProfileId(request.profileImageId());
        }

        userRepository.create(user);

        userRepository.create(new UserStatus(user.getId(), Instant.now()));
    }

    @Override
    public User read(UUID id) {
        User user = userRepository.read(id);
        if (user == null) {
            throw new IllegalArgumentException(" --해당 유저를 찾을 수 없습니다.");
        }
        return user;
    }

    @Override
    public List<User> readAll() {
        return userRepository.readAll();
    }

    @Override
    public List<User> readByName(String name) {
        return userRepository.readByName(name);
    }

    @Override
    public User update(UUID id, User update) {
        User user = userRepository.read(id);
        if (user == null) {
            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
        }
        user.update(update);
        return user;
    }

    @Override
    public boolean delete(UUID id, String password) {
        User user = userRepository.read(id);
        if (user == null) {
            throw new IllegalArgumentException(" --해당 유저를 찾을 수 없습니다.");
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("!!유저 탈퇴 실패!! --- 비밀번호 불일치");
            return false;
        }

        boolean deleted = userRepository.delete(id);
        if (deleted) {
            removeUserFromChannels(user);
            System.out.println("유저 탈퇴 성공");
        }
        return deleted;
    }

    @Override
    public void removeUserFromChannels(User user) {
        for (Channel channel : channelRepository.readAll()) {
            Set<User> members = new HashSet<>(channel.getMembers());
            if (members.remove(user)) {
                channel.setMembers(members);
                channelRepository.update(channel.getId(), channel);

            }
        }
    }

    private void validateDuplicateNameAndEmail(UserCreateRequest request) {
        List<User> users = userRepository.readAll();
        boolean exists = users.stream()
                .anyMatch((u -> u.getEmail().equals(request.email())));
        if(exists) {
            throw new IllegalArgumentException(" --- 이미 등록된 이메일입니다.");
        }
    }

    //User를 UserDto로 변환
    private UserDto toDto(User user, boolean includeStatus) {
        boolean isOnline = false;

        if (includeStatus) {
            UserStatus status = userStatusRepository.readByUserId(user.getId());
            isOnline = status != null && status.isOnlineNow();
        }

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileId(),
                isOnline
        );
    }
}
