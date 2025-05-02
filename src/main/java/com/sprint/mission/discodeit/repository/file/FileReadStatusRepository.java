package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.DataInitializer.READSTATUS_FILE_PATH;

public class FileReadStatusRepository implements ReadStatusRepository {
    private final String FILE_PATH = READSTATUS_FILE_PATH;

    private Map<UUID, ReadStatus> data = loadData();

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
    public ReadStatus update(ReadStatus update) {
        ReadStatus readStatus = this.data.get(update.getId());
        readStatus.updateLastReadTime(update.getLastReadTime());
        return readStatus;
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
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[binaryContent] 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[binaryContent] 저장된 파일이 없습니다. 새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[binaryCotent] 데이터 불러오기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
