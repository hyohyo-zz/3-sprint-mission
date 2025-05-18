package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User createUser(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> Users() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateUser(UUID id, User updatedUser) {
        if (!data.containsKey(id)) {
            return null;
        }
        User existing = data.get(id);
        existing.setName(updatedUser.getName());
        existing.setGender(updatedUser.getGender());
        existing.setEmail(updatedUser.getEmail());
        existing.setPassword(updatedUser.getPassword());
        existing.setPhone(updatedUser.getPhone());
        existing.setUpdatedAt(System.currentTimeMillis());
        return existing;
    }

    @Override
    public boolean deleteUser(UUID id) {
        return data.remove(id) != null;
    }

    public List<User> findByName(String name) {
        return data.values().stream()
                .filter(user-> user.getName().contains(name))
                .toList();
    }

    public Map<String, List<User>> groupByGender() {
        return data.values().stream()
                .collect(Collectors.groupingBy(User::getGender));
    }

}