package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class BasicUserService implements UserService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicUserService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        validateDuplicateEmail(user);
        userRepository.create(user);
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
        return userRepository.delete(id, password);
    }

    @Override
    public void removeUserFromChannels(User user) {
        for (Channel channel : channelRepository.readAll()) {
            Set<User> members = new HashSet<>(channel.getMembers());
            if (members.remove(user)) {
                Channel updatedChannel = new Channel(
                        channel.getChannelName(),
                        channel.getKeyUser(),
                        channel.getCategory(),
                        members
                );
                channelRepository.update(channel.getId(), updatedChannel);
            }
        }
    }

    private void validateDuplicateEmail(User user) {
        List<User> users = userRepository.readAll();
        boolean exists = users.stream()
                .anyMatch((u -> u.getEmail().equals(user.getEmail())));
        if(exists) {
            throw new IllegalArgumentException(" --- 이미 등록된 이메일입니다.");
        }
    }
}
