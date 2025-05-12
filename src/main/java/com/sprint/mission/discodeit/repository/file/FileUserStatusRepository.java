package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.time.Instant;
import java.util.*;


public class FileUserStatusRepository implements UserStatusRepository {
    private final String filePath;
    private Map<UUID, UserStatus> data;

    public FileUserStatusRepository(DiscodeitProperties properties) {
        if (properties.getFilePath() == null) {
            System.out.println(ErrorMessages.format("[UserStatus]", ErrorMessages.ERROR_FILE_PATH_NULL));
        }
        this.filePath = properties.getFilePath() + "/userstatus.ser";
        this.data = new HashMap<>();
    }

    // 파일 있으면 불러오기
    @PostConstruct
    public void init() {
        this.data = loadData();
    }

    @Override
    public UserStatus create(UserStatus userstatus) {
        data.put(userstatus.getUserId(), userstatus);
        saveData();
        return userstatus;
    }

    @Override
    public UserStatus find(UUID id) {
        return this.data.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .findFirst();
    }

    @Override
    public boolean delete(UUID id) {
        boolean removed = data.remove(id) != null;
        saveData();
        return removed;
    }

    @Override
    public boolean deleteByUserId(UUID userId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .map(UserStatus::getUserId)
                .toList();

        boolean deleted = false;
        for(UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }
        saveData();
        return deleted;
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println(ErrorMessages.format("[UserStatus]", ErrorMessages.ERROR_SAVE));
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(ErrorMessages.format("[UserStatus]", ErrorMessages.ERROR_NOT_FOUND));
            System.out.println("새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ErrorMessages.format("[UserStatus]", ErrorMessages.ERROR_LOAD));
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
