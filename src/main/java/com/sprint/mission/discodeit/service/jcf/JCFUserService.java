package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void create(User user) {
        this.data.put(user.getId(), user);
    }

    @Override
    public User read(UUID id) {
        return this.data.get(id);
    }

    @Override
    public User update(UUID id, User update) {
        User selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    @Override
    public List<User> read(String name) {
        return data.values().stream()
                .filter(user-> user.getName().contains(name))
                .toList();
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    //성별 그룹화
    public Map<String, List<User>> groupByGender() {
        return data.values().stream()
                .collect(Collectors.groupingBy(User::getGender));
    }
}