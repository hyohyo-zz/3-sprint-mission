package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.util.DataInitializer.USERSTATUS_FILE_PATH;

public class FileUserStatusRepository implements UserStatusRepository {
    private final String FILE_PATH = USERSTATUS_FILE_PATH;

    private Map<UUID, UserStatus> data = loadData();

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
    public UserStatus update(UserStatus update) {
        UserStatus userStatus = data.get(update.getUserId());
        userStatus.updateOnlineStatus(userStatus.isOnline());
        userStatus.updatelastOnline();;
        saveData();
        return update;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
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
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[userStatus] 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[userStatus] 저장된 파일이 없습니다. 새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[userStatus] 데이터 불러오기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
