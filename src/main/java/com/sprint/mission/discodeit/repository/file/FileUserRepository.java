package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.*;


public class FileUserRepository implements UserRepository {
    private final String filePath;
    private ChannelRepository channelRepository;
    private Map<UUID, User> data;

    public FileUserRepository(DiscodeitProperties properties, ChannelRepository channelRepository) {
        if (properties.getFilePath() == null) {
            System.out.println(ErrorMessages.format("[User]", ErrorMessages.ERROR_FILE_PATH_NULL));
        }
        this.filePath = properties.getFilePath() + "/user.ser";
        this.channelRepository = channelRepository;
        this.data = new HashMap<>();
    }

    // 파일 있으면 불러오기
    @PostConstruct
    public void init() {
        this.data = loadData();
    }

    @Override
    public User create(User user) {
        this.data.put(user.getId(), user);
        saveData();
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
        return this.data.remove(id) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUserName(String userName) {
        return this.findAll().stream()
                .anyMatch(user -> user.getName().equals(userName));
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println(ErrorMessages.format("[User]", ErrorMessages.ERROR_SAVE));
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(ErrorMessages.format("[User]", ErrorMessages.ERROR_NOT_FOUND));
            System.out.println("새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ErrorMessages.format("[User]", ErrorMessages.ERROR_LOAD));

            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }

}
