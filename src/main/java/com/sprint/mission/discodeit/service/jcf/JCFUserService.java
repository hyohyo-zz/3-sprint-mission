package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

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
    public List<User> read(String name) {
        return data.values().stream()
                .filter(user-> user.getName().contains(name))
                .toList();
    }

    //유저 전체 조회
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    //유저 수정
    @Override
    public User update(UUID id, User update) {
        User selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    //유저 삭제
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