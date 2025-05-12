package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    //유저 생성
    @Override
    public User create(User user) {
        this.data.put(user.getId(), user);
        return user;
    }

    //유저 아이디 조회
    @Override
    public Optional<User> find(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    //유저 이름으로 조회
    @Override
    public Optional<User> findByUserName(String name) {
        return data.values().stream()
                .filter(user -> Objects.equals(user.getName(), name))
                .findFirst();
    }

    //유저 전체 조회
    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    //유저 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUserName(String userName) {
        return this.findAll().stream().anyMatch(user -> user.getName().equals(userName));
    }
}


