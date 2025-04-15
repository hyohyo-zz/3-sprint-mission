package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();
    private final ChannelService channelService;

    public JCFUserRepository(JCFChannelService channelService) {
        this.channelService = channelService;
    }

    //유저 생성
    @Override
    public void create(User user) {
        this.data.put(user.getId(), user);
    }

    //유저 아이디 조회
    @Override
    public User read(UUID id) {
        return this.data.get(id);
    }

    //유저 이름으로 조회
    @Override
    public List<User> readByName(String name) {
        List<User> result = data.values().stream()
                .filter(user -> user.getName().contains(name))
                .collect(Collectors.toList());
        return result;
    }

    //유저 전체 조회
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    //유저 수정
    @Override
    public User update(UUID id, User update) {
        User user = this.data.get(id);
        user.update(update);
        return user;
    }

    //유저 삭제
    @Override
    public boolean delete(UUID id, String password) {
        return data.remove(id) != null;
    }

}
