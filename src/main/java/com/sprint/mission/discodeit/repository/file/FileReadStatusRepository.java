package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class FileReadStatusRepository implements ReadStatusRepository {
    private final String filePath;
    private Map<UUID, ReadStatus> data;

    public FileReadStatusRepository(DiscodeitProperties properties) {
        if (properties.getFilePath() == null) {
            System.out.println(ErrorMessages.format("[ReadStatus]", ErrorMessages.ERROR_FILE_PATH_NULL));
        }
        this.filePath = properties.getFilePath() + "/readstatus.ser";
        this.data = new HashMap<>();
    }

    // 파일 있으면 불러오기
    @PostConstruct
    public void init() {
        this.data = loadData();
    }

    @Override
    public ReadStatus create(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        saveData();
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return this.data.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(file -> Objects.equals(file.getChannelId(), channelId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id) {
        return this.data.remove(id) != null;
    }

    @Override
    public boolean deleteByUserId(UUID userId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .map(ReadStatus::getId)
                .toList();

        boolean deleted = false;
        for(UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }
        saveData();
        return deleted;
    }

    @Override
    public boolean deleteByChannelId(UUID channelId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getChannelId(), channelId))
                .map(ReadStatus::getId)
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
            System.out.println(ErrorMessages.format("[ReadStatus]", ErrorMessages.ERROR_SAVE));
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(ErrorMessages.format("[ReadStatus]", ErrorMessages.ERROR_NOT_FOUND));
            System.out.println("새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ErrorMessages.format("[ReadStatus]", ErrorMessages.ERROR_LOAD));
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
