package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }
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
        return this.data.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    //유저 삭제
    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
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


